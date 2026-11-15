#!/bin/bash
set -e

# ========================================================
# DHADIYA SHOWROOM MANAGEMENT PLATFORM (USMS ARCHITECTURE)
# MASTER STARTUP SCRIPT
# ========================================================

echo "========================================================"
echo " Starting Dhadiya Showroom Management Platform"
echo "========================================================"

# 1. Environment & Java 25 Setup
export JAVA_HOME="/Users/apple/.vscode/extensions/redhat.java-1.58.0-darwin-arm64/jre/25.0.4.1-macosx-aarch64"
export PATH="$JAVA_HOME/bin:/opt/homebrew/bin:$PATH"

echo "Using Java: $($JAVA_HOME/bin/java -version 2>&1 | head -n 1)"

# 2. Check / Start PostgreSQL 16
echo ""
echo "[1/4] Ensuring PostgreSQL 16 is running..."
if ! /opt/homebrew/bin/brew services list | grep postgresql@16 | grep -q "started"; then
    /opt/homebrew/bin/brew services start postgresql@16
    sleep 2
fi
echo "PostgreSQL 16 is running on port 5432."

# 3. Start dhadiya-tenants (Port 8081) - Global Master Database
echo ""
echo "[2/4] Starting dhadiya-tenants on port 8081 (Master Database: dhadiya_global)..."
cd /Users/apple/zora/dhadiya/dhadiya-tenants
$JAVA_HOME/bin/java -jar target/dhadiya-tenants-1.0.0-SNAPSHOT.jar --spring.profiles.active=postgres > ../tenants.log 2>&1 &
TENANTS_PID=$!
echo "dhadiya-tenants started (PID: $TENANTS_PID). Logs: tenants.log"

# Wait for Master Service to initialize before starting Flow Engine
echo "Waiting for Master Tenant Registry on port 8081..."
for i in {1..15}; do
    if curl -s http://localhost:8081/db/tenant >/dev/null 2>&1; then
        echo "Master Tenant Registry is ready."
        break
    fi
    sleep 1
done

# 4. Start dhadiya-flow (Port 8082) - Dynamic Operations & DDL Engine
echo ""
echo "[3/4] Starting dhadiya-flow on port 8082 (Dynamic Tenant Routing)..."
cd /Users/apple/zora/dhadiya/dhadiya-flow
$JAVA_HOME/bin/java -jar target/dhadiya-flow-1.0.0-SNAPSHOT.jar --spring.profiles.active=postgres > ../flow.log 2>&1 &
FLOW_PID=$!
echo "dhadiya-flow started (PID: $FLOW_PID). Logs: flow.log"

# 5. Start dhadiya-portal (Port 4200) - Angular 18+ Web Console
echo ""
echo "[4/4] Starting dhadiya-portal on port 4200..."
cd /Users/apple/zora/dhadiya/dhadiya-portal
npm start > ../portal.log 2>&1 &
PORTAL_PID=$!
echo "dhadiya-portal started (PID: $PORTAL_PID). Logs: portal.log"

echo ""
echo "========================================================"
echo " Dhadiya Platform is up and running!"
echo " - Portal Web Console: http://localhost:4200"
echo " - Global Master DB:   http://localhost:8081 (dhadiya_global)"
echo " - Flow Engine API:    http://localhost:8082 (Tenant Routing)"
echo " - PostgreSQL 16:      localhost:5432 (User: postgres)"
echo " - pgAdmin 4:          Launch with: open -a 'pgAdmin 4'"
echo "========================================================"
