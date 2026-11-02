#!/usr/bin/env bash
set -euo pipefail

PORT=6000
TEST_SCRIPT="test-script.txt"

mvn compile

# Start the software bus
mvn exec:java \
  -Dexec.mainClass=SoftwareBus.SBMain \
  "-Dexec.args=$PORT" &
BUS_PID=$!

cleanup() {
  kill "$BUS_PID" "${TH_PID:-}" 2>/dev/null || true
}

trap cleanup EXIT INT TERM

# give the server time to start listening
sleep 1

# start the test harness
mvn exec:java \
  -Dexec.mainClass=TestHarness.THMain \
  "-Dexec.args=127.0.0.1 $PORT" &
TH_PID=$!

# start the actual javafx gui for the panels
mvn javafx:run \
  "-Djavafx.args=127.0.0.1 $PORT $TEST_SCRIPT"