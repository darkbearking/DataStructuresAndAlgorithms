package com.troila.test;

public class Test {

	class User{
		private String id, name;
		private int age;
		
		public User(String id, String name, int age) {
			this.id = id;
			this.name = name;
			this.age = age;
		}
		
		public String toString() {
			String returnStr = null;
			returnStr = "id = [" + this.id + "], name = [" + this.name + "], age = [" + this.age + "]";
			return returnStr;
		}
		
		public User addAge(User user) {
			//請原諒我這裡沒有使用AtomicInteger
			user.setAge(user.getAge() + 1);
			return user;
		}
		
		public void minusAge(User user) {
			user.setAge(user.getAge() - 2);
		}

		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
	}
	
	public void testUser() {
		User user1 = new User("5", "張三", 30);
		User user2 = user1;
		
		System.out.println(user1.toString());
		System.out.println(user2.toString());
		
		user2.setAge(18);
		
		System.out.println(user1.toString());
		System.out.println(user2.toString());
		
		user1 = new User("5", "張三", 30);
		user2 = new User("5", "張三", 30);
		
		System.out.println(user1 == user2);
		
		user1.addAge(user1);
		System.out.println(user1.toString());
		
		user1.minusAge(user1);
		System.out.println(user1.toString());
	} 
	
	public static void main(String[] args) {
		Test test = new Test();
		test.testUser();
		
	}
}
