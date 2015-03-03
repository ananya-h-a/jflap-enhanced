package enhanced.action;
public class NOT extends TreeNode
{

   public NOT()
   {
      numArgs=1;
   
      args=new TreeNode[numArgs];
      
      argType=new Class[numArgs];
      
      argType[numArgs-1]= Boolean.class;
      
   	setClassName("Not");
      
    }
    
   public  Class returnType()
    {
    
      return  Boolean.class;
     
     }
     
     public Object evaluate()
     {
       try {
         boolean x=(Boolean)args[0].evaluate();
         return new Boolean(!x);
       } catch(Exception e) {
         return null;
       }
     }        
}
