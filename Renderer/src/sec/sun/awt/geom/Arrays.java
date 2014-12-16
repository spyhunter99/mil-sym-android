/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.sun.awt.geom;

/**
 *
 * @author Michael Deutch
 * An inexpensive hack to the Arrays class so that we can sort the curves.
 * Instead of using a comparator object we are using a specific instance of its compare method
 * as required for adding Areas.
 */
public class Arrays {
    private static final int INSERTIONSORT_THRESHOLD = 7;
    public static void sort(Object[]a) {
        Object[]aux=new Object[a.length];
        int n=a.length;
        //for(int j=0;j<a.length;j++)
        for(int j=0;j<n;j++)
            aux[j]=a[j];
            
        mergeSort(aux, a, 0, a.length, 0);
    }
    private static void swap(Object[] x, int a, int b) {
	Object t = x[a];
	x[a] = x[b];
	x[b] = t;
    }
    private static int compare(Object o1, Object o2) {
        CurveObject c1 = ((Edge) o1).getCurve();
        CurveObject c2 = ((Edge) o2).getCurve();
        double v1, v2;
        if ((v1 = c1.getYTop()) == (v2 = c2.getYTop())) {
            if ((v1 = c1.getXTop()) == (v2 = c2.getXTop())) {
                return 0;
            }
        }
        if (v1 < v2) {
            return -1;
        }
        return 1;
    }
    private static void mergeSort(Object[] src,
				  Object[] dest,
				  int low, int high, int off){
				  //Comparator c) {
	int length = high - low;

	// Insertion sort on smallest arrays
	if (length < INSERTIONSORT_THRESHOLD) {
	    for (int i=low; i<high; i++)
		for (int j=i; j>low && compare(dest[j-1], dest[j])>0; j--)
		    swap(dest, j, j-1);
	    return;
	}

        // Recursively sort halves of dest into src
        int destLow  = low;
        int destHigh = high;
        low  += off;
        high += off;
        int mid = (low + high) >>> 1;
        //mergeSort(dest, src, low, mid, -off, c);
        //mergeSort(dest, src, mid, high, -off, c);
        mergeSort(dest, src, low, mid, -off);
        mergeSort(dest, src, mid, high, -off);

        // If list is already sorted, just copy from src to dest.  This is an
        // optimization that results in faster sorts for nearly ordered lists.
        if (compare(src[mid-1], src[mid]) <= 0) 
        {
           System.arraycopy(src, low, dest, destLow, length);
           //arraycopy(src, low, dest, destLow, length);
           //return;
        }

        // Merge sorted halves (now in src) into dest
        for(int i = destLow, p = low, q = mid; i < destHigh; i++) {
            if (q >= high || p < mid && compare(src[p], src[q]) <= 0)
                dest[i] = src[p++];
            else
                dest[i] = src[q++];
        }
    }
    

    /**
     * @param      src      the source array.
     * @param      srcPos   starting position in the source array.
     * @param      dest     the destination array.
     * @param      destPos  starting position in the destination data.
     * @param      length   the number of array elements to be copied.
     * @exception  IndexOutOfBoundsException  if copying would cause
     *               access of data outside array bounds.
     * @exception  ArrayStoreException  if an element in the <code>src</code>
     *               array could not be stored into the <code>dest</code> array
     *               because of a type mismatch.
     * @exception  NullPointerException if either <code>src</code> or
     *               <code>dest</code> is <code>null</code>.
     */
    public static void arraycopy(Object[] src,  int  srcPos,
                                        Object[] dest, int destPos,
                                        int length)
    {
        int j=0;
        for(j=0;j<length;j++)        
            dest[j+destPos]=src[srcPos+j];        
    }
   
}
