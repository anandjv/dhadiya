#!/bin/bash
echo "Stopping all Dhadiya platform services..."
pkill -f "dhadiya-tenants" || true
pkill -f "dhadiya-flow" || true
pkill -f "ng serve" || true
pkill -f "dhadiya-portal" || true
echo "Dhadiya services stopped."
