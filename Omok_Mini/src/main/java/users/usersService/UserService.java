package users.usersService;

import users.SignUpForm;
import users.User;
import users.usersRepository.UserRepository;

public class UserService {

	private static final UserRepository USERREPOSITORY = UserRepository.getInstance();
	
	//싱글톤 
	private static volatile UserService instance;

    private UserService() { }

    public static UserService getInstance() {
        if (instance == null) {
            synchronized (UserRepository.class) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }
    
    public User login(String userId, String userPW) {
        User user = USERREPOSITORY.findById(userId);
        if (user == null) {
        	return null;
        }
        if (!user.getUserPw().equals(userPW)) {
            return null;
        }
        return user;
    }
    
    public boolean SignUp(SignUpForm signupform) {	
    	String id = signupform.getUserId();
    	
        User user = USERREPOSITORY.findById(id);
        if (user != null) {
        	System.out.println("이미 존재하는 ID입니다.");
        	return false; 
        } // false 반환 시 메서드 종료되기 때문에 else 없어도 될 것 같음
        
        // DTO -> VO 변환 (User 객체 생성)
        User newUser = new User();
        newUser.setUserId(signupform.getUserId());
        newUser.setUserPw(signupform.getUserPw());
        newUser.setEmail(signupform.getEmail());
        newUser.setNickname(signupform.getNickname());
        
        // 리포지토리에 저장 요청
        User savedUser = USERREPOSITORY.save(newUser);
        
        return savedUser != null; // savedUser가 null이 아니라면 true 반환
    }
    
    // 탈퇴 기능 추가
    public boolean withdraw(String userId) {
    	int result = USERREPOSITORY.delete(userId);
    	return result > 0;
    }
}
