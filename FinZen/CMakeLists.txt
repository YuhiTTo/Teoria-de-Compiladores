cmake_minimum_required(VERSION 3.10)

set(CMAKE_BUILD_TYPE Debug)
project(FinZen)

set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

# Base directory setup (assuming standard layout)
set(MY_BASE_DIR "${CMAKE_CURRENT_SOURCE_DIR}")

# ANTLR Configuration
# Adjust these paths if necessary based on your specific environment
set(ANTLR_EXECUTABLE "/usr/share/java/antlr-complete.jar") 
set(ANTLR_INCLUDE_DIR "/usr/include/antlr4-runtime")

# Fallback for ANTLR jar if not found (optional, based on example)
if(NOT EXISTS "${ANTLR_EXECUTABLE}")
    message(STATUS "ANTLR jar not found at ${ANTLR_EXECUTABLE}, checking local...")
    set(ANTLR_EXECUTABLE "${MY_BASE_DIR}/antlr-4.13.2-complete.jar")
endif()

# find_package(ANTLR) removed to avoid warning, handled later with REQUIRED
# If FindANTLR.cmake is not in module path, we might need to download it or rely on system
# For now, assuming it works or we might need to add the download step from the example if it fails.
# Adding the download step for safety as per example 2:
if(NOT EXISTS "${MY_BASE_DIR}/FindANTLR.cmake")
    message(STATUS "Downloading FindANTLR.cmake...")
    file(DOWNLOAD
    "https://raw.githubusercontent.com/antlr/antlr4/master/runtime/Cpp/cmake/FindANTLR.cmake"
    "${MY_BASE_DIR}/FindANTLR.cmake"
    STATUS DOWNLOAD_STATUS)
endif()
list(APPEND CMAKE_MODULE_PATH ${MY_BASE_DIR})
find_package(ANTLR REQUIRED)

message(STATUS "Found ANTLR: ${ANTLR_VERSION}")

# LLVM Configuration
find_package(LLVM REQUIRED CONFIG COMPONENTS Core Support CodeGen Passes TransformUtils)
message(STATUS "Found LLVM ${LLVM_PACKAGE_VERSION}")
message(STATUS "Using LLVMConfig.cmake in: ${LLVM_DIR}")

add_definitions(${LLVM_DEFINITIONS})

# Generate ANTLR C++ files
file(GLOB GFOURS "src/*.g4")
antlr_target(${PROJECT_NAME} ${GFOURS} LEXER PARSER VISITOR)

# Include directories
include_directories(
    ${ANTLR_INCLUDE_DIR}
    ${ANTLR_${PROJECT_NAME}_OUTPUT_DIR}
    ${LLVM_INCLUDE_DIRS}
    src
)

# Source files
file(GLOB SOURCES "src/*.cpp")

# Executable
add_executable(${PROJECT_NAME} ${SOURCES} ${ANTLR_${PROJECT_NAME}_CXX_OUTPUTS})

# Link libraries
target_link_libraries(${PROJECT_NAME} 
    antlr4-runtime 
    LLVM
)

# C++ Standard
target_compile_features(${PROJECT_NAME} PRIVATE cxx_std_17)
