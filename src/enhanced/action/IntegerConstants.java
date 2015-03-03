package enhanced.action;

public class IntegerConstants extends TreeNode
{
private Integer value;

   public IntegerConstants(int val)
   {
      value = val;
      args = new TreeNode[0];  
      argType = new Class[0];
      setClassName("" + value);

      
    }
    
   public  Class returnType()
    {
    
      return Integer.class;
     
     }
     
     public Object evaluate()
     {
      	return Integer.valueOf(value);
     }
     
     public void setArg(Integer val)
     {
      	value = val;
			setClassName("" + value);
     }     
}
