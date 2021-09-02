mybatis-puls 的querywrapper优化

User user = new User();

user.setId(1);

user.setUserName("小");


优化前

QueryWrapper queryWrapper1 = new QueryWrapper();

queryWrapper1.eq("user_name",user.getUserName());

queryWrapper1.eq("id",user.getId());

List list1 = userService.list(queryWrapper1);


优化后

QueryWrapper queryWrapper = new Repository().fielList(user);

List<User> list = userService.list(queryWrapper);
  
list.forEach(user1 -> {
     System.out.println(user1.toString());
  
 });
