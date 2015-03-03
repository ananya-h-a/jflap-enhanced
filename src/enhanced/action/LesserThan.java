package enhanced.action;
public class LesserThan extends TreeNode
{

   public LesserThan()
   {
      numArgs=2;
   
      args=new TreeNode[numArgs];
      
      argType=new Class[numArgs];
      
      argType[numArgs-2]=argType[numArgs-1]= Integer.class;
      
     setClassName("is lesser than"); 
      
    }
    
   public  Class returnType()
    {
    
      return  Boolean.class;
     
     }
     
     public Object evaluate()
     {
       Integer X = (Integer)args[0].evaluate();
       Integer Y = (Integer)args[1].evaluate();
       return new Boolean(X != null && Y != null
                       && X.intValue() < Y.intValue());
     }
         
}
