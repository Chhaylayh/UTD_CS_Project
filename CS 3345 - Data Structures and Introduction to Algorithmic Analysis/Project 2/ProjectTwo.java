//Programing Assignment 2
//Name: Chhay Lay Heng
//Class: CS3345.004

import java.util.*;

public class ProjectTwo {
    
    public static void main(String[] args) {
        
        // Array Size
        int[] sizes = {1000, 10000, 100000}; 
        
        // Generate Random Number
        Random randomNum = new Random(); 

        for (int size : sizes)  {
            
            int[] array = new int[size];
                
           // Generate random array
           for (int i = 0; i < size; i++) {
               array[i] = randomNum.nextInt();
           }

            // Benchmarking MergeSort
            long startTime1 = System.currentTimeMillis();
            mergeSort(array, 0, array.length - 1);
            long endTime1 = System.currentTimeMillis();
            long mergeSortTime;
            mergeSortTime = endTime1 - startTime1;

            // Reset Array
            for (int i = 0; i < size; i++) {
                array[i] = randomNum.nextInt();
            }

            // Benchmarking Quicksort
            long startTime2 = System.currentTimeMillis();
            quickSort(array, 0, array.length - 1);
            long endTime2 = System.currentTimeMillis();
            long quickSortTime = endTime2 - startTime2;

            System.out.println("ArraySize: " + size);
            System.out.println("Mergesort Time: " + mergeSortTime + " ms");
            System.out.println("Quicksort Time: " + quickSortTime + " ms");
            System.out.println("-----------------------\n");
        }
        
        int[] randomSort = randomSequence(5);
        System.out.println("Random Sorted: " + Arrays.toString(randomSort));
        
        int[] almostSort = almostSequence(5);
        System.out.println("Almost Sorted: " + Arrays.toString(almostSort));
    }

    public static void merge(int[] array, int left, int mid, int right) {
        
        int midLeft = mid - left + 1;
        int midRight = right - mid;

        // Create temporary arrays
        int[] leftArray = new int[midLeft];
        int[] rightArray = new int[midRight];

        for (int i = 0; i < midLeft; i++) {
            leftArray[i] = array[left + i];
        }

        for (int j = 0; j < midRight; j++) {
            rightArray[j] = array[mid + 1 + j];
        }

        int i = 0;
        int j = 0;

        while (i < midLeft && j < midRight) {
            if (leftArray[i] <= rightArray[j]) {
                array[left] = leftArray[i];
                i++;
            } 
            else {
                array[left] = rightArray[j];
                j++;
            }
            left++;
        }

        // Copy the remaining elements of leftArray[]
        while (i < midLeft) {
            array[left] = leftArray[i];
            i++;
            left++;
        }

        // Copy the remaining elements of rightArray[]
        while (j < midRight) {
            array[left] = rightArray[j];
            j++;
            left++;
        }
    }

    public static void mergeSort(int[] array, int left, int right) {
        
        if (left < right) {
            
            int mid = left + (right - left) / 2;
            
            // Sort first and second halves
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    public static int partition(int[] array, int left, int right) {
        
        // Median-of-three pivot selection
        int mid = left + (right - left) / 2;

        if (array[mid] < array[left]) {
            int temp1 = array[mid];
            array[mid] = array[left];
            array[left] = temp1;
        }

        if (array[right] < array[left])  {
            int temp2 = array[right];
            array[right] = array[left];
            array[left] = temp2;
        }

        if (array[mid] < array[right]) {
            int temp3 = array[mid];
            array[mid] = array[right];
            array[right] = temp3;
        }

        // Choose pivot as the median-of-three
        int pivot = array[right];
        int i = left - 1;

        for (int j = left; j < right; j++) {
            if (array[j] <= pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        int temp = array[i + 1];
        
        array[i + 1] = array[right];
        array[right] = temp;

        return i + 1;
    }

    public static void quickSort(int[] array, int left, int right) {
        if (left < right) {
            // Use insertion sort for small subarrays
            if (right - left <= 15)
                insertionSort(array, left, right);
            else {
                int pivotIndex = partition(array, left, right);  
                quickSort(array, left, pivotIndex - 1);
                quickSort(array, pivotIndex + 1, right);
            }
        }
    }

    public static void insertionSort(int[] array, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int pivotElement = array[i];
            int j = i - 1;
            
            while (j >= left && array[j] > pivotElement) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = pivotElement;
        }
    }
    
    public static int[] randomSequence(int size) {
        int[] array = new int[size];
        Random random = new Random();

        for (int i = 0; i < size; i++)
            array[i] = random.nextInt();

        return array;
    }
    
    public static int[] almostSequence(int size) {
        int[] array = new int[size];
        Random random = new Random();

        for (int i = 0; i < size; i++)
            array[i] = i;

        // Swap two elements to make it almost sorted
        int index1 = random.nextInt(size);
        int index2 = random.nextInt(size);
        int temp = array[index1];
        
        array[index1] = array[index2];
        array[index2] = temp;
       
        return array;
    }
}
