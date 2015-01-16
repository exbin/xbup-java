/*
  HexEdit2Text class
  Written by: Keith Fenske, http://www.psc-consulting.ca/fenske/
  Monday, 27 October 2008
  Java class name: HexEdit2User.java
  This class is part of HexEdit2
  Copyright (c) 2008 by Keith Fenske.  Released under GNU Public License.

  This class listens to input from the user and passes back event parameters to
  static methods in the main class.

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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class HexEdit2User implements ActionListener, Transferable
{
    private final HexEdit2 hexEdit;
  /* empty constructor */

  public HexEdit2User(HexEdit2 hexEdit) { this.hexEdit = hexEdit; }

  /* button listener, dialog boxes, etc */

  public void actionPerformed(ActionEvent event)
  {
    hexEdit.userButton(event);
  }

  /* clipboard data transfer */

  public Object getTransferData(DataFlavor flavor)
    throws IOException, UnsupportedFlavorException
  {
    if (hexEdit.clipString == null)
      throw new IOException("no clipboard string created");
    else if (flavor.equals(DataFlavor.stringFlavor))
      return(hexEdit.clipString);
    else
      throw new UnsupportedFlavorException(flavor);
  }

  public DataFlavor[] getTransferDataFlavors()
  {
    final DataFlavor[] result = { DataFlavor.stringFlavor };
    return(result);
  }

  public boolean isDataFlavorSupported(DataFlavor flavor)
  {
    return(flavor.equals(DataFlavor.stringFlavor));
  }

} // end of HexEdit2User class
