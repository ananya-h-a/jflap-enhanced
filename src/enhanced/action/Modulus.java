
package enhanced.action;
public class Modulus extends TreeNode
{

   public Modulus()
   {
      numArgs = 2;
      
      args = new TreeNode[numArgs];
      
      argType = new Class[numArgs];
      
      argType[numArgs-2] = argType[numArgs-1] = Integer.class;
      
		setClassName( "mod" );
   
      
      
    }
    
   public  Class returnType()
     {
    
      return Integer.class;
     
     }
     
    public Object evaluate()
    {
      Integer X = (Integer) args[0].evaluate();
      Integer Y = (Integer) args[1].evaluate();
      try {
        return new Integer(X.intValue() % Y.intValue());
      } catch(Exception e) {
        return null;
      }
    }
}
