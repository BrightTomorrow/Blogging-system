function custom_close(){
      if(confirm("Are you sure to shut off the system?")){
          window.opener=null;
          window.open('','_self');
          window.close();
      }
      else{
     }
 }