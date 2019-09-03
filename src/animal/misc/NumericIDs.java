package animal.misc;

public class NumericIDs
{
  private int[] ids;

  public NumericIDs(int id)
  {
    ids = new int[1];
    ids[0] = id;
  }

  public NumericIDs(int[] targetIDs)
  {
    setIDs(targetIDs);
  }

  public int getIDAt(int index)
  {
    if (ids==null || index<0 || index>ids.length)
      return -1;
    return ids[index];
  }

  public int[] getIDs()
  {
    return ids;
  }

  public int[] getIDSlice(int start, int end)
  {
    if (ids == null || start<0|| start>end || start>ids.length
	|| end>ids.length)
      return null;
    int[] slice = new int[end-start+1];
    System.arraycopy(ids, start, slice, 0, end-start+1);
    return slice;
  }

  public void setIDAt(int id, int index)
  {
    if (ids != null && index>-1 &&  index<ids.length)
      ids[index] = id;
  }

  public void setIDs(int[] targetIDs)
  {
    ids = new int[targetIDs.length];
    System.arraycopy(targetIDs, 0, ids, 0, ids.length);
  }

  public int size()
  {
    return(ids != null) ? ids.length : 0;
  }


  public String toString()
  {
    if (ids == null || ids[0] == -1)
      return null;
    int i;
    StringBuilder sb = new StringBuilder();
    for (i=0; i<ids.length-1; i++)
      sb.append(ids[i]).append(' ');
    sb.append(ids[i]);
    return sb.toString();
  }
}
