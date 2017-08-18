/*
  Sql指令学习
*/

/*
  #sql指令
    指令中的key用于在程序中读取
    String sql = Db.getSql("findRichMan");
*/
#sql('findRichMan')
  SELECT * FROM account WHERE balance >= 300;
#end

/*
  #namespace指令
  String sql = Db.getSql("china.findOneMan");
*/
#namespace("china")
  #sql('findOneMan')
    SELECT * FROM account WHERE id = ?
  #end
#end
