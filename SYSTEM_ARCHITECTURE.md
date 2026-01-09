# Integrated Fraud Detection System - Architecture & File Structure

## Overview
This repository integrates the complete Ai-based-fraud-detection-system with advanced framework utilities for robust, scalable fraud detection capabilities.

## Complete File Inventory

### Fraud Detection Core Components
From: https://github.com/soravpatel10/Ai-based-fraud-detection-system

1. **Database.java** - SQLite database connection management
2. **Exception.java** - Custom exception handling
3. **FraudDetector.java** - Main fraud detection algorithm
4. **FraudDetectorBase.java** - Abstract base class for detection strategies
5. **Generic CSV Loader.java** - CSV file parsing and data loading
6. **Main.java** - Application entry point with JavaFX UI initialization
7. **MainController.java** - UI controller for fraud detection interface
8. **Multithreading + Synchronization.java** - Concurrent processing utilities
9. **Transaction.java** - Transaction data model
10. **TransactionDAO.java** - Data access object for transactions
11. **User.java** - User account data model
12. **ZScoreFraudDetector.java** - Z-score based anomaly detection algorithm
13. **app.fxml** - JavaFX UI definition file
14. **ui screenshot.png** - Application UI screenshot

### Advanced Framework Utilities (Added for Enhanced Functionality)

#### Core Components
1. **CoreFeatures.java** - Feature lifecycle management
2. **DataValidator.java** - Comprehensive input validation framework
3. **ErrorHandler.java** - Centralized error handling and logging
4. **EventHandler.java** - Event-driven architecture for inter-component communication

#### Framework Documentation
1. **IMPLEMENTATION_GUIDE.md** - Detailed framework feature documentation
2. **FRAUD_DETECTION_INTEGRATION.md** - Integration specifications
3. **RUN_INSTRUCTIONS.md** - Comprehensive setup and execution guide

## System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      User Interface                         │
│                    (JavaFX - app.fxml,                      │
│                  MainController.java)                       │
└──────────────────────────┬──────────────────────────────────┘
                           │
         ┌─────────────────┼─────────────────┐
         │                 │                 │
         v                 v                 v
┌─────────────────┐ ┌──────────────┐ ┌─────────────┐
│ Input Handler   │ │ EventHandler │ │ CoreFeatures│
│ - Validation    │ │ - Publishing │ │ - Lifecycle │
│ - Parsing       │ │ - Listeners  │ │ - Control   │
└────────┬────────┘ └──────┬───────┘ └─────┬───────┘
         │                 │               │
         └─────────────────┼───────────────┘
                           v
                 ┌──────────────────┐
                 │  FraudDetector   │
                 │  Multi-Strategy  │
                 │  Analysis        │
                 └────────┬─────────┘
                          │
         ┌────────────────┼────────────────┐
         │                │                │
         v                v                v
┌──────────────┐ ┌────────────────┐ ┌──────────────┐
│FraudDetector │ │ZScoreFraudDet. │ │  Custom      │
│Base (Abstract)│ │(Implementation)│ │Algorithms    │
└──────────────┘ └────────────────┘ └──────────────┘
         │                │                │
         └────────────────┼────────────────┘
                          v
                ┌──────────────────────┐
                │  Data Access Layer   │
                │  - TransactionDAO    │
                │  - Database.java     │
                │  - CSV Loader        │
                └──────────┬───────────┘
                           v
                ┌──────────────────────┐
                │  Data Models         │
                │  - User.java         │
                │  - Transaction.java  │
                └──────────┬───────────┘
                           v
                ┌──────────────────────┐
                │  Error & Logging     │
                │  - ErrorHandler      │
                │  - Exceptions        │
                └──────────────────────┘
```

## Component Relationships & Data Flow

### 1. User Input Processing
- **UI Layer** (MainController.java, app.fxml) → Captures user input
- **Validation** (DataValidator.java) → Validates email, phone, amounts
- **Parsing** (Generic CSV Loader) → Loads transaction data
- **Events** (EventHandler.java) → Publishes INPUT_RECEIVED events

### 2. Fraud Detection Processing
- **Core Processing** (FraudDetector.java) → Multi-factor analysis
  - Amount threshold checking
  - Frequency analysis
  - Location anomaly detection
  - User history review
  
- **Specialized Detection**
  - ZScoreFraudDetector → Statistical anomaly detection
  - Custom algorithms → Domain-specific rules
  
- **Feature Management** (CoreFeatures.java) → Controls detection modules

### 3. Data Management
- **Transaction Model** (Transaction.java) → Represents transaction data
- **User Model** (User.java) → Represents user profiles
- **DAO Layer** (TransactionDAO.java) → Database operations
- **Database** (Database.java) → SQLite persistence

### 4. Event Publishing & Logging
- **EventHandler** → Publishes:
  - FRAUD_DETECTED
  - FRAUD_SUSPECTED
  - TRANSACTION_PROCESSED
  - USER_FLAGGED
  
- **ErrorHandler** → Logs all exceptions and errors with context

## File Dependencies & Integration Map

```
Main.java
  ├── Depends: MainController.java, app.fxml
  ├── Uses: Database.java
  ├── Uses: EventHandler.java
  └── Uses: ErrorHandler.java

MainController.java
  ├── Uses: FraudDetector.java
  ├── Uses: DataValidator.java
  ├── Uses: Transaction.java, User.java
  ├── Uses: EventHandler.java
  └── Uses: Generic CSV Loader

FraudDetector.java (Core Component)
  ├── Extends: FraudDetectorBase.java
  ├── Uses: TransactionDAO.java
  ├── Uses: Database.java
  ├── Uses: ErrorHandler.java
  └── Uses: EventHandler.java

ZScoreFraudDetector.java (Specialized)
  ├── Extends: FraudDetectorBase.java
  ├── Uses: Transaction.java, User.java
  └── Uses: ErrorHandler.java

TransactionDAO.java
  ├── Uses: Database.java
  ├── Uses: Transaction.java
  ├── Uses: ErrorHandler.java
  └── Uses: Multithreading + Synchronization

DataValidator.java (Framework Utility)
  ├── Static validation methods
  ├── No dependencies
  └── Used by: Multiple components for input validation

ErrorHandler.java (Framework Utility)
  ├── Centralized error management
  ├── No dependencies
  └── Used by: All components for error handling

EventHandler.java (Framework Utility)
  ├── Observer pattern implementation
  ├── No dependencies
  └── Used by: Core components for inter-component communication

CoreFeatures.java (Framework Utility)
  ├── Feature lifecycle management
  ├── No dependencies
  └── Used by: MainController for feature control
```

## Integration Points

### 1. Input Validation Integration
```
User Input → DataValidator → Validation Result
                ↓
        If Invalid: Log with ErrorHandler
        If Valid: Process through FraudDetector
```

### 2. Fraud Detection Integration
```
Transaction + User → FraudDetector
                        ↓
            Risk Score Calculation
                        ↓
        Publish FRAUD_DETECTED Event → EventHandler
                        ↓
        Log Result → ErrorHandler
```

### 3. Feature Control Integration
```
CoreFeatures (Activate/Deactivate)
    ↓
MainController checks feature status
    ↓
Enables/Disables FraudDetector algorithms
```

## Technology Stack

- **Language**: Java 8+
- **Database**: SQLite
- **UI Framework**: JavaFX
- **Concurrency**: Java Multithreading & Synchronization
- **Design Patterns**: Strategy, Observer, DAO, Singleton

## Build & Deployment

### Compilation Order (Dependencies First)
1. Framework utilities (DataValidator, ErrorHandler, EventHandler, CoreFeatures)
2. Data models (User, Transaction)
3. Database layer (Database, TransactionDAO)
4. Fraud detection algorithms (FraudDetectorBase, FraudDetector, ZScoreFraudDetector)
5. UI components (MainController, app.fxml)
6. Entry point (Main)

### Compilation Command
```bash
javac -d bin *.java
```

### Execution Command
```bash
java -cp bin Main
```

## Contributing

When adding new fraud detection algorithms:
1. Extend FraudDetectorBase.java
2. Implement calculateFraudRisk() and getThreshold() methods
3. Add error handling with ErrorHandler
4. Publish events using EventHandler
5. Update this documentation

## References

- Original Project: https://github.com/soravpatel10/Ai-based-fraud-detection-system
- Framework: https://github.com/Harshit-ops-rgb/advanced-project-framework
- Documentation: See IMPLEMENTATION_GUIDE.md and RUN_INSTRUCTIONS.md

---

**Status**: Production Ready
**Last Updated**: January 2026
**Version**: 1.0.0
