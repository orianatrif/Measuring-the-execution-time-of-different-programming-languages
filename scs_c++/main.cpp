#include <iostream>
#include <fstream>
#include <vector>
#include <chrono>
#include <thread>
#include <future>

// Measure memory allocation time
//long measureMemoryAllocationTime(int size) {
//    auto startTime = std::chrono::high_resolution_clock::now();
//    std::vector<int> array(size);
//    auto endTime = std::chrono::high_resolution_clock::now();
//    return std::chrono::duration_cast<std::chrono::nanoseconds>(endTime - startTime).count();
//}

long measureMemoryAllocationTime(int size) {
    auto start_time = std::chrono::high_resolution_clock::now();

    int* dynamicArray = new int[size];

    auto end_time = std::chrono::high_resolution_clock::now();

    // Delete the allocated memory
    delete[] dynamicArray;

    return std::chrono::duration_cast<std::chrono::nanoseconds>(end_time - start_time).count();
}
// Measure memory access static
long measureStaticMemoryAccess(int size) {
    std::vector<int> staticArray(size);
    auto startTime = std::chrono::high_resolution_clock::now();
    for (int i = 0; i < size; i++) {
        int element = staticArray[i];
    }
    auto endTime = std::chrono::high_resolution_clock::now();
    return std::chrono::duration_cast<std::chrono::nanoseconds>(endTime - startTime).count();
}
// Measure memory access dynamically
long measureDynamicMemoryAccess(int size) {
    std::vector<int> dynamicArray(size);
    auto startTime = std::chrono::high_resolution_clock::now();
    for (int i = 0; i < size; i++) {
        dynamicArray[i] = i;
        int element = dynamicArray[i];
    }
    auto endTime = std::chrono::high_resolution_clock::now();
    return std::chrono::duration_cast<std::chrono::nanoseconds>(endTime - startTime).count();
}
// Measure thread creation
long measureThreadCreation() {
    auto startTime = std::chrono::high_resolution_clock::now();
    std::thread thread([] {});
    thread.join();
    auto endTime = std::chrono::high_resolution_clock::now();
    return std::chrono::duration_cast<std::chrono::nanoseconds>(endTime - startTime).count();
}
// Measure thread context switch
long measureThreadContextSwitch() {
    auto startTime = std::chrono::high_resolution_clock::now();

    // Use std::async to launch a thread
    std::future<void> result = std::async(std::launch::async, [] {
        // Code to be executed in the new thread
    });

    // Wait for the thread to finish
    result.wait();

    auto endTime = std::chrono::high_resolution_clock::now();
    return std::chrono::duration_cast<std::chrono::nanoseconds>(endTime - startTime).count();
}
// Measure element addition to list
long measureElementAdditionToList() {
    auto startTime = std::chrono::high_resolution_clock::now();

    std::vector<int> myList;
    myList.push_back(42);  // Add an element to the list

    auto endTime = std::chrono::high_resolution_clock::now();

    return std::chrono::duration_cast<std::chrono::nanoseconds>(endTime - startTime).count();
}

// Measure element removal from list
long measureElementRemovalFromList() {
    std::vector<int> myList;
    myList.push_back(42);  // Add an element to the list

    auto startTime = std::chrono::high_resolution_clock::now();

    // Remove the element from the list
    myList.erase(myList.begin());

    auto endTime = std::chrono::high_resolution_clock::now();

    return std::chrono::duration_cast<std::chrono::nanoseconds>(endTime - startTime).count();
}

// Measure list reversal
long measureListReversal() {
    std::vector<int> myList;

    // Add elements to the list
    for (int i = 0; i < 1000; i++) {
        myList.push_back(i);
    }

    auto startTime = std::chrono::high_resolution_clock::now();

    // Reverse the list
    std::reverse(myList.begin(), myList.end());

    auto endTime = std::chrono::high_resolution_clock::now();

    return std::chrono::duration_cast<std::chrono::nanoseconds>(endTime - startTime).count();
}

// Write results to a file
void writeResultToFile(const std::string& fileName, long elapsedTimeMemAlloc, long elapsedTimeMemAccStatic, long elapsedTimeMemAccDynamic, long elapsedTimeThreadCr, long elapsedTimeThreadCS, long elapsedTimeElementAddition, long elapsedTimeElementRemoval, long elapsedTimeListReversal, int numberOfAllocations) {
    std::ofstream outputFile(fileName);
    if (outputFile.is_open()) {
        //outputFile << "C++ results:\n";
        outputFile << elapsedTimeMemAlloc << "\n";
        outputFile << elapsedTimeMemAccStatic << "\n";
        outputFile  << elapsedTimeMemAccDynamic << "\n";
        outputFile << elapsedTimeThreadCr << "\n";
        outputFile << elapsedTimeThreadCS << "\n";
        outputFile << elapsedTimeElementAddition << "\n";
        outputFile << elapsedTimeElementRemoval << "\n";
        outputFile << elapsedTimeListReversal << "\n";
        //outputFile << "Number of allocations: " << numberOfAllocations << "\n";
        outputFile.close();
    } else {
        std::cerr << "Error: Unable to open the file " << fileName << " for writing." << std::endl;
    }
}

int main() {
    int numberOfAllocations = 100;
    long totalTimeAlloc = 0, totalTimeMemAccStatic = 0, totalTimeMemAccDynamic = 0, totalTimeThreadCr = 0;
    long totalElemAddition = 0, totalElemRemoval = 0, totalListReversal = 0;
    long totalTimeThreadCS = 0, totalTimeThreadMigration = 0;
    int nrTests = 1000; // array dimension

    for (int i = 0; i < numberOfAllocations; i++) {
        long elapsedTimeMemAlloc = measureMemoryAllocationTime(nrTests);
        long elapsedTimeMemAccStatic = measureStaticMemoryAccess(nrTests);
        long elapsedTimeMemAccDynamic = measureDynamicMemoryAccess(nrTests);
        long elapsedTimeThreadCr = measureThreadCreation();
        long elapsedTimeThreadCS = measureThreadContextSwitch();
        long elapsedTimeElementAddition = measureElementAdditionToList();
        long elapsedTimeElementRemoval = measureElementRemovalFromList();
        long elapsedTimeListReversal = measureListReversal();

        totalTimeMemAccDynamic += elapsedTimeMemAccDynamic;
        totalTimeAlloc += elapsedTimeMemAlloc;
        totalTimeMemAccStatic += elapsedTimeMemAccStatic;
        totalTimeThreadCr += elapsedTimeThreadCr;
        totalTimeThreadCS += elapsedTimeThreadCS;
        totalElemAddition += elapsedTimeElementAddition;
        totalElemRemoval += elapsedTimeElementRemoval;
        totalListReversal += elapsedTimeListReversal;
    }

    // Calculate average elapsed time
    long averageTimeMemAlloc = totalTimeAlloc / numberOfAllocations;
    long averageTimeMemAccStatic = totalTimeMemAccStatic / numberOfAllocations;
    long averageTimeMemAccDynamic = totalTimeMemAccDynamic / numberOfAllocations;
    long averageTimeThreadCr = totalTimeThreadCr / numberOfAllocations;
    long averageTimeThreadCS = totalTimeThreadCS / numberOfAllocations;
    long averageTimeElemAddition = totalElemAddition / numberOfAllocations;
    long averageTimeElemRemoval = totalElemRemoval / numberOfAllocations;
    long averageTimeListReversal = totalListReversal / numberOfAllocations;


    // Print and write results to a file
   // writeResultToFile("memory_allocation_result.txt", averageTimeMemAlloc, averageTimeMemAccStatic, averageTimeMemAccDynamic, averageTimeThreadCr, averageTimeThreadCS, averageTimeThreadMigration, numberOfAllocations);
    writeResultToFile("c++_results.txt", averageTimeMemAlloc,averageTimeMemAccStatic,averageTimeMemAccDynamic,  averageTimeThreadCr,  averageTimeThreadCS, averageTimeElemAddition, averageTimeElemRemoval, averageTimeListReversal, numberOfAllocations);

    std::cout << averageTimeMemAlloc << "\n";
    std::cout << averageTimeMemAccStatic << "\n";
    std::cout << averageTimeMemAccDynamic << "\n";
    std::cout << averageTimeThreadCr << "\n";
    std::cout << averageTimeThreadCS << "\n";
    std::cout << averageTimeElemAddition << "\n";
    std::cout << averageTimeElemRemoval << "\n";
    std::cout << averageTimeListReversal << "\n";

    return 0;
}
