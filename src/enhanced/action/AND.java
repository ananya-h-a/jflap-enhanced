package enhanced.action;
public class AND extends TreeNode
{

   public AND()
   {
      numArgs=2;
   
      args=new TreeNode[numArgs];
      
      argType=new Class[numArgs];
      
      argType[numArgs-2]=argType[numArgs-1]= Boolean.class;
      
     setClassName("And"); 
      
    }
    
   public  Class returnType()
    {
    
      return  Boolean.class;
     
     }
     
     public Object evaluate()
     {
       try {
         boolean x=(Boolean)args[0].evaluate();
         if(x) {
           return args[1].evaluate();
         } else {
           return Boolean.FALSE;
         }
       } catch(Exception e) {
         return null;
       }
     }
         
}
