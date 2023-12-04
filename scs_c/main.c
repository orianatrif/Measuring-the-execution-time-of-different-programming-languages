#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>


#define NUM_ITERATIONS 1000000

void *dummyFunction(void *arg) {
    return NULL;
}


long measureMemoryAllocationTime(int size) {
    struct timespec start_time, end_time;

    clock_gettime(CLOCK_MONOTONIC, &start_time);

    int* dynamicArray = (int*)malloc(size * sizeof(int));

    clock_gettime(CLOCK_MONOTONIC, &end_time);

    // Free the allocated memory
    free(dynamicArray);

    // Calculate the elapsed time in nanoseconds
    return (end_time.tv_sec - start_time.tv_sec) * 1e9 + (end_time.tv_nsec - start_time.tv_nsec);
}

// Measure memory access static
long measureStaticMemoryAccess(int size) {
    int *staticArray = (int *)malloc(size * sizeof(int));

    struct timespec start, end;
    clock_gettime(CLOCK_MONOTONIC, &start);

    for (int i = 0; i < size; i++) {
        int element = staticArray[i];
    }

    clock_gettime(CLOCK_MONOTONIC, &end);
    free(staticArray);

    return (end.tv_sec - start.tv_sec) * 1000000000L + (end.tv_nsec - start.tv_nsec);
}

// Measure memory access dynamically
long measureDynamicMemoryAccess(int size) {
    int *dynamicArray = (int *)malloc(size * sizeof(int));

    struct timespec start, end;
    clock_gettime(CLOCK_MONOTONIC, &start);

    for (int i = 0; i < size; i++) {
        dynamicArray[i] = i;
        int element = dynamicArray[i];
    }

    clock_gettime(CLOCK_MONOTONIC, &end);
    free(dynamicArray);

    return (end.tv_sec - start.tv_sec) * 1000000000L + (end.tv_nsec - start.tv_nsec);
}

// Measure thread creation
long measureThreadCreation() {
    struct timespec start, end;
    clock_gettime(CLOCK_MONOTONIC, &start);

    pthread_t thread;
    pthread_create(&thread, NULL, dummyFunction, NULL);
    pthread_join(thread, NULL);

    clock_gettime(CLOCK_MONOTONIC, &end);

    return (end.tv_sec - start.tv_sec) * 1000000000L + (end.tv_nsec - start.tv_nsec);
}

// Measure thread context switch
long measureThreadContextSwitch() {
    struct timespec start, end;
    clock_gettime(CLOCK_MONOTONIC, &start);

    // Code to be executed in the new thread
    pthread_t thread;
    pthread_create(&thread, NULL, dummyFunction, NULL);
    pthread_join(thread, NULL);

    clock_gettime(CLOCK_MONOTONIC, &end);

    return (end.tv_sec - start.tv_sec) * 1000000000L + (end.tv_nsec - start.tv_nsec);
}

// Measure element addition to list
long measureElementAdditionToList() {
    struct timespec start, end;
    clock_gettime(CLOCK_MONOTONIC, &start);

    int *myList = (int *)malloc(sizeof(int));
    myList[0] = 42; // Add an element to the list

    clock_gettime(CLOCK_MONOTONIC, &end);
    free(myList);

    return (end.tv_sec - start.tv_sec) * 1000000000L + (end.tv_nsec - start.tv_nsec);
}

// Measure element removal from list
long measureElementRemovalFromList() {
    int *myList = NULL;
    int initialCapacity = 1;
    int size = 0;

    myList = (int *)malloc(initialCapacity * sizeof(int));

    if (myList == NULL) {
        // Handle memory allocation failure
        return -1;
    }

    // Add an element to the list
    myList[size++] = 42;

    struct timespec startTime, endTime;

    clock_gettime(CLOCK_MONOTONIC, &startTime);

    // Remove the element from the list
    // In C, removing an element from an array involves shifting elements
    // to fill the gap left by the removed element.
    for (int i = 0; i < size - 1; i++) {
        myList[i] = myList[i + 1];
    }

    size--; // Update the size of the array

    clock_gettime(CLOCK_MONOTONIC, &endTime);

    free(myList); // Don't forget to free the allocated memory

    // Calculate the time elapsed in nanoseconds
    return (endTime.tv_sec - startTime.tv_sec) * 1e9 + (endTime.tv_nsec - startTime.tv_nsec);
}


// Measure list reversal
long measureListReversal() {
    struct timespec start, end;

    int *myList = (int *)malloc(1000 * sizeof(int));

    // Add elements to the list
    for (int i = 0; i < 1000; i++) {
        myList[i] = i;
    }

    clock_gettime(CLOCK_MONOTONIC, &start);

    // Reverse the list
    for (int i = 0, j = 999; i < j; i++, j--) {
        int temp = myList[i];
        myList[i] = myList[j];
        myList[j] = temp;
    }

    clock_gettime(CLOCK_MONOTONIC, &end);
    free(myList);

    return (end.tv_sec - start.tv_sec) * 1000000000L + (end.tv_nsec - start.tv_nsec);
}

// Write results to a file
void writeResultToFile(const char *fileName, long elapsedTimeMemAlloc, long elapsedTimeMemAccStatic, long elapsedTimeMemAccDynamic, long elapsedTimeThreadCr, long elapsedTimeThreadCS, long elapsedTimeElementAddition, long elapsedTimeElementRemoval, long elapsedTimeListReversal, int numberOfAllocations) {
    FILE *outputFile = fopen(fileName, "w");

    if (outputFile != NULL) {
        //fprintf(outputFile, "C results:\n");
        fprintf(outputFile, "%ld\n", elapsedTimeMemAlloc);
        fprintf(outputFile, "%ld\n", elapsedTimeMemAccStatic);
        fprintf(outputFile, "%ld\n", elapsedTimeMemAccDynamic);
        fprintf(outputFile, "%ld\n", elapsedTimeThreadCr);
        fprintf(outputFile, "%ld\n", elapsedTimeThreadCS);
        fprintf(outputFile, "%ld\n", elapsedTimeElementAddition);
        fprintf(outputFile, "%ld\n", elapsedTimeElementRemoval);
        fprintf(outputFile, "%ld\n", elapsedTimeListReversal);

        fclose(outputFile);
    } else {
        perror("Error: Unable to open the file for writing");
    }
}

int main() {
    int numberOfAllocations = 1000;
    long totalTimeAlloc = 0, totalTimeMemAccStatic = 0, totalTimeMemAccDynamic = 0, totalTimeThreadCr = 0;
    long totalElemAddition = 0, totalElemRemoval = 0, totalListReversal = 0;
    long totalTimeThreadCS = 0;
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

//    printf("Average memory allocation took: %ld nanoseconds\n", averageTimeMemAlloc);
//    printf("Average memory access static took: %ld nanoseconds\n", averageTimeMemAccStatic);
//    printf("Average memory access dynamic took: %ld nanoseconds\n", averageTimeMemAccDynamic);
//    printf("Average thread creation took: %ld nanoseconds\n", averageTimeThreadCr);
//    printf("Average thread context switch took: %ld nanoseconds\n", averageTimeThreadCS);
//    printf("Average list addition took: %ld nanoseconds\n", averageTimeElemAddition);
//    printf("Average list removal took: %ld nanoseconds\n", averageTimeElemRemoval);
//    printf("Average list reversal took: %ld nanoseconds\n", averageTimeListReversal);

    printf("%ld\n", averageTimeMemAlloc);
    printf("%ld\n", averageTimeMemAccStatic);
    printf("%ld\n", averageTimeMemAccDynamic);
    printf("%ld\n", averageTimeThreadCr);
    printf("%ld\n", averageTimeThreadCS);
    printf("%ld\n", averageTimeElemAddition);
    printf("%ld\n", averageTimeElemRemoval);
    printf("%ld\n", averageTimeListReversal);

    // Print and write results to a file
    writeResultToFile("c_results.txt", averageTimeMemAlloc, averageTimeMemAccStatic, averageTimeMemAccDynamic, averageTimeThreadCr, averageTimeThreadCS, averageTimeElemAddition, averageTimeElemRemoval, averageTimeListReversal, numberOfAllocations);
   // printf("%ld\n", averageTimeListReversal);
    return 0;
}
