# **Distributed SAGA Orchestrator — README**

A comprehensive, production-oriented, and educational implementation of the **SAGA Pattern** designed to coordinate multi-step transactions across modular services using a **Command–State Machine**, **Chain of Responsibility (CoR) Handler Flow**, and **asynchronous orchestration**. This project models a simplified yet realistic orchestration engine that executes business steps, handles partial failures, triggers compensations, and maintains predictable state transitions.

The orchestrator is built using **Spring Boot**, **ExecutorService-based async dispatch**, and **CoR-based handler chaining**, making it accessible for learning and extensible for real-world adaptations such as adding Kafka Outbox, DB-backed intent stores, circuit breakers, and distributed compensators.

---

## **🎯 Purpose of the SAGA Orchestrator**

This module demonstrates how long-running, multi-service operations can be executed **reliably** without distributed transactions. It provides a clear, deterministic mechanism to:

* Drive forward (RECEIVE → EXECUTE → EXTRACT) business operations
* Trigger compensations automatically on failure
* Retry transient failures
* Produce observable state transitions
* Isolate service concerns via handlers
* Execute async operations using dedicated thread pools

It bridges the gap between conceptual SAGA theory and practical, testable implementation.

---

## **📐 Core Design Highlights**

### **1. Chain of Responsibility–Driven Flow**

Handlers (`IdentityHandler → KycHandler → ScoreHandler`) orchestrate which command to run next, maintaining local state (`prevCommandState`) to determine RECEIVE/EXECUTE/EXTRACT/COMPENSATE flows.

### **2. Command-State Execution Model**

`CommandData` instructs the orchestrator which service method to invoke, while `State` (PASS/ERROR/RETRY) returned from services determines the next pipeline action.

### **3. Async Execution with CompletableFuture**

Each service has a dedicated thread pool.
The orchestrator invokes service commands on the corresponding executor, enabling true parallelism and realistic latency simulation.

### **4. Service & Executor Mapping**

Service key → DataService implementation
Service key → Executor pool
This creates dynamic routing between handler outputs and concrete service logic.

### **5. In-Memory Intent and Data Store**

For educational clarity, an in-memory `DataStore` and lightweight `Database` are used for journaling audit logs and live state inspection.

---

## **📂 Folder Structure**

```
Distributed-SAGA-Orchestrator/
│
├─ api/                    # REST endpoints (start saga, get status, fetch data)
├─ business/               # Domain service stubs (Identity, KYC, Score)
├─ config/                 # Executor and service mapping configuration
├─ core/                   # CoR Handlers for each stage
├─ data/                   # CommandData, Data model, enums
├─ database/               # In-memory audit + data persistence
├─ model/                  # Interfaces (Handler, DataService)
├─ service/                # Orchestrator + Handler chain Manager
└─ utils/                  # ID generators and helpers
```

---

## **📊 SAGA Orchestrator — Phase Roadmap**

Below is an abstract progression table for SAGA evolution, from foundational prototype to distributed deployment.

| Phase       | Name                                  | Abstract Objective                                                      | Core Additions                                                                                                 | Complexity              |  Folder           |
| ----------- | ------------------------------------- | ----------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------- | ----------------------- | -------------------- |
| **Phase A** | **In-Process Orchestrator**           | Build core Command–State machine with CoR handlers and async execution. | Async `CompletableFuture`, handler chaining, service mapping, compensation, in-memory store.                   | Skeleton Architecture | [PhaseA](https://github.com/VishuKalier2003/System-Design-Components/tree/main/ChainOfResponsibility/S-02DistributedSaga/phaseA)


---

## **📘 Summary**

This SAGA Orchestrator serves as a complete educational model for understanding both the **control-flow mechanics** and **operational resiliency** required in multi-service transactions. Its phased architecture allows beginners to grasp fundamentals while enabling experienced engineers to scale the design into a distributed, production-ready system.

If you want a **diagram-rich architectural guide**, **starter code templates**, or **Phase C distributed expansion**, feel free to request — contributions and ideas are always welcome!
