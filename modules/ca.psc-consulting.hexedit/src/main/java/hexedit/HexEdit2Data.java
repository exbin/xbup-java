/*
  HexEdit2Data class
  Written by: Keith Fenske, http://www.psc-consulting.ca/fenske/
  Monday, 27 October 2008
  Java class name: HexEdit2Data.java
  This class is part of HexEdit2
  Copyright (c) 2008 by Keith Fenske.  Released under GNU Public License.

  This class maintains an array of byte data.  Data elements are expected to be
  nibbles with values from 0 to 15, although this is not enforced.  Calls are
  similar to the standard Java classes for List and Vector.  The order of the
  parameters and their interpretation may differ.

  A hex editor is a classic compromise between speed and memory requirements.
  The smallest way of storing file data is as a single array of bytes, with one
  array element per file byte.  The fastest way of reading and replacing nibble
  data is as a single array with one element per nibble.  Inserting or deleting
  elements in a single array would require (on average) shuffling half of the
  elements each time.  This is impractical for large file sizes.  The fastest
  way to insert and delete is to have each nibble as a separate element in a
  height-balanced binary tree, which wastes too much space for small elements.

  A rough estimate shows that Java is capable of moving at least 10 million
  bytes per second in a tight <for> loop:

      byte[] second = new byte[first.length];
      for (int i = 0; i < first.length; i ++)
        second[i] = first[i];

  Times were obtained with Java 1.4.2 and Windows 2000 on an Intel Pentium 4
  processor at 2.4 GHz, about five years old by current standards, so most
  other machines have equal or greater performance.  Users will notice GUI
  delays of more than 1/10 of a second.  That means moving much less than a
  megabyte per keystroke.

  In a hex editor, most of the time, the user is only viewing the data.  When
  changes are made, they occur around a cursor location: insert, delete, or
  replace.  The algorithms below assume that it is tolerable to have a small
  delay when first making changes at a new cursor location.  After that, most
  changes will happen without a delay.

  The data is split into two byte arrays of nibble values.  The "left" array
  has data to the left (or before) an imaginary editing cursor.  The "right"
  array has data to the right (or after) this editing cursor.  (The editing
  cursor is derived from the user's real cursor, but tends to lag behind.)
  Backward deletes (the Backspace key) always remove trailing elements from the
  left array.  Forward deletes always remove leading elements from the right
  array.  Inserts go at the end of the left array.  Fetches and replacements go
  to whichever array is appropriate.  When the editing cursor changes, the
  arrays are split at a new location to make changes possible.

  This is neither optimal for speed nor for memory storage.  It is relatively
  fast and easy to implement.  The default Java virtual machine should be able
  to handle files up to 10 megabytes (given the default maximum heap size of 60
  or so megabytes), which is considerably more than the one megabyte that could
  be done previously when each nibble was stored as an element in a Vector
  object.

  The caller can help by always inserting or deleting sequentially from a
  cursor location.  For example, when deleting a byte with the Backspace key
  (backward direction), delete the low-order nibble first, then the high-order
  nibble.  When deleting a byte with the Delete key (forward), delete the high-
  order nibble, then the low-order nibble.

  GNU General Public License (GPL)
  --------------------------------
  HexEdit2 is free software: you can redistribute it and/or modify it under the
  terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License or (at your option) any later
  version.  This program is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY or
  FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
  more details.

  You should have received a copy of the GNU General Public License along with
  this program.  If not, see the http://www.gnu.org/licenses/ web page.
*/

package hexedit;

class HexEdit2Data
{
  /* constants */

  static private final int PadSIZE = 4096; // extra space added to arrays

  /* instance variables */

  private byte[] leftArray;       // elements before imaginary editing cursor
  private int leftUsed;           // number of data elements actually used in
                                  // ... <leftArray> starting at index zero
  private byte[] rightArray;      // elements after imaginary editing cursor
  private int rightBegin;         // index of first used element
  private int rightEnd;           // index *after* last used element
  private int totalSize;          // total number of used elements

  /* class constructor */

  public HexEdit2Data(int capacity)
  {
    super();                      // initialize our superclass first (Object)

    if (capacity >= 0)            // initial capacity can't be negative
    {
      leftArray = new byte[PadSIZE]; // create empty left array
      rightArray = new byte[capacity + PadSIZE]; // create empty right array
      leftUsed = rightBegin = rightEnd = totalSize = 0;
    }
    else
    {
      throw new IllegalArgumentException("HexEdit2Data capacity " + capacity
        + " can't be negative");
    }
  }


/*
  append() method

  Append a new element to the end of the data.  Produces the same result as
  inserting or replacing immediately after the last element.
*/
  void append(int value)
  {
    put(size(), value);           // use common processing
  }


/*
  clear() method

  Without allocating new arrays, make them look empty.  All data becomes lost.
  This method is not used anywhere in this program, but is included for a sense
  of completeness.
*/
  void clear()
  {
    leftUsed = rightBegin = rightEnd = totalSize = 0;
  }


/*
  delete() method

  Delete one element at a given location.  Following elements appear to be
  shuffled left, when in fact, they usually don't move: a pointer changes.
*/
  void delete(int position)
  {
    refreshSize();                // refresh total number of data elements
    if ((position < 0) || (position >= totalSize)) // is position within range?
      error(position);            // no, indicate an error
    else if (position == (leftUsed - 1))
      leftUsed --;                // throw away one position on the left
    else if (position == leftUsed)
      rightBegin ++;              // throw away leading position on the right
    else if (position == (totalSize - 1))
      rightEnd --;                // throw away trailing position on the right
    else
    {
      if (split(position))        // break arrays at this position
        rightBegin ++;            // throw away leading position on the right
    }
  } // end of delete() method


/*
  error() method

  Throws an "array index out of bounds" exception.  The caller gives us the
  invalid array index to report.

  The caller should not assume that this terminates processing, because the
  error may be thrown or may later be changed to a System.err.print() call.
*/
  private void error(int position)
  {
    throw new ArrayIndexOutOfBoundsException("HexEdit2Data index " + position
      + " is not from 0 to " + size());
  }


/*
  get() method

  Return the value of an element at a given location, as an unsigned integer.
*/
  int get(int position)
  {
    int result;                   // byte value as unsigned integer

    if ((position < 0) || (position >= size())) // is position within range?
    {
      error(position);            // no, indicate an error
      result = -1;                // in case error() returns to us
    }
    else if (position < leftUsed) // if data can be found in left array
      result = ((int) leftArray[position]) & HexEdit2.BYTE_MASK;
    else                          // otherwise data must be in right array
      result = ((int) rightArray[position - leftUsed + rightBegin])
        & HexEdit2.BYTE_MASK;

    return(result);               // give caller whatever we could find

  } // end of get() method


/*
  insert() method

  Insert a new element at a given location.  The element at that location will
  be shuffled right and will be after the inserted element.  For convenience,
  the location may be immediately after the last element, and is equivalent to
  calling the append() method.
*/
  void insert(int position, int value)
  {
    refreshSize();                // refresh total number of data elements
    if ((position < 0) || (position > totalSize)) // is position within range?
      error(position);            // no, indicate an error
    else if ((position == leftUsed) && (leftUsed < leftArray.length))
      leftArray[leftUsed ++] = (byte) value;
    else if ((position == totalSize) && (rightEnd < rightArray.length))
      rightArray[rightEnd ++] = (byte) value;
    else
    {
      if (split(position))        // break arrays at this position
        leftArray[leftUsed ++] = (byte) value; // insert new element on left
    }
  } // end of insert() method


/*
  put() method

  Replace the element at a given location with a new value.  For convenience,
  the location may be immediately after the last element, and is equivalent to
  calling the append() method.
*/
  void put(int position, int value)
  {
    refreshSize();                // refresh total number of data elements
    if ((position < 0) || (position > totalSize)) // is position within range?
      error(position);            // no, indicate an error
    else if (position < leftUsed) // replace element in left array?
      leftArray[position] = (byte) value;
    else if (position < totalSize) // replace element in right array?
      rightArray[position - leftUsed + rightBegin] = (byte) value;
    else if (rightEnd < rightArray.length) // append element to right array?
      rightArray[rightEnd ++] = (byte) value;
    else
    {
      if (split(position))        // put everything in left array (totalSize)
        leftArray[leftUsed ++] = (byte) value; // insert new element on left
    }
  } // end of put() method


/*
  refreshSize() method

  Internal method to recalculate the total number of data elements used in both
  arrays.  This is an easy but frequently used calculation.
*/
  private void refreshSize()
  {
    totalSize = leftUsed + rightEnd - rightBegin;
  }


/*
  size() method

  Return the total number of data elements.
*/
  int size()
  {
    refreshSize();                // refresh total number of data elements
    return(totalSize);            // and return that value to the caller
  }


/*
  split() method

  Split the left and right arrays at a given location.  Any positions less than
  the given location will be in the left array; positions greater than or equal
  will be in the right array.

  This method is never called frivolously: either the arrays are currently
  divided at the wrong position, or there isn't enough room left for inserting.
  This is the one method that must be very efficient.

  While "out of memory" errors are likely, we don't catch those here.  That is
  done by much higher-level methods in the "main" class that are able to cancel
  entire transactions and properly report to the user.  We just make sure that
  our internal data will be in a consistent state even if an exception occurs
  (that is, work on temporary copies, replace global copies when done).
*/
  private boolean split(int position)
  {
    int count;                    // number of bytes that will be copied
    int from;                     // index where bytes will be copied from
    int i;                        // index variable
    byte[] newLeftArray, newRightArray; // new byte arrays that we are making
    int newLeftUsed, newRightUsed; // number of bytes used in new arrays
    boolean result;               // true if split successful, false otherwise

    refreshSize();                // refresh total number of data elements
    result = false;               // assume that split will fail
    if ((position < 0) || (position > totalSize)) // is position within range?
      error(position);            // no, indicate an error
    else                          // must be a valid split
    {
      /* Copy portion of left array that remains in left array.  The <count>
      variables below may be negative if a situation does not apply. */

      newLeftArray = new byte[position + PadSIZE]; // allocate new left array
      count = Math.min(leftUsed, position); // number of bytes to copy
      newLeftUsed = 0;            // where they go in new left array
      for (i = 0; i < count; i ++)
        newLeftArray[newLeftUsed ++] = leftArray[i];

      /* Copy portion of left array that comes from right array. */

      count = position - leftUsed; // number of bytes to copy
      from = rightBegin;          // where we start copying bytes from
      for (i = 0; i < count; i ++)
        newLeftArray[newLeftUsed ++] = rightArray[from ++];

      /* Copy portion of right array that comes from left array. */

      newRightArray = new byte[totalSize - position + PadSIZE];
      count = leftUsed - position;
      from = position;
      newRightUsed = 0;           // where bytes go in new right array
      for (i = 0; i < count; i ++)
        newRightArray[newRightUsed ++] = leftArray[from ++];

      /* Copy portion of right array that remains in right array. */

      count = totalSize - Math.max(leftUsed, position);
      from = rightEnd - count;
      for (i = 0; i < count; i ++)
        newRightArray[newRightUsed ++] = rightArray[from ++];

      /* Replace existing instance variables with our new variables. */

      leftArray = newLeftArray;
      leftUsed = newLeftUsed;
      rightArray = newRightArray;
      rightBegin = 0;
      rightEnd = newRightUsed;

      result = true;              // declare that split was successful
    }
    return(result);               // tell caller if split was successful

  } // end of split() method

} // end of HexEdit2Data class

