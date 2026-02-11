package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.config.JwtService;
import sn.axa.apiaxacnaas.dto.LoginDTO;
import sn.axa.apiaxacnaas.dto.UserCreateDTO;
import sn.axa.apiaxacnaas.dto.UserDTO;
import sn.axa.apiaxacnaas.entities.Agence;
import sn.axa.apiaxacnaas.entities.Partner;
import sn.axa.apiaxacnaas.entities.Role;
import sn.axa.apiaxacnaas.entities.User;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.UserMapper;
import sn.axa.apiaxacnaas.repositories.AgenceRepository;
import sn.axa.apiaxacnaas.repositories.PartnerRepository;
import sn.axa.apiaxacnaas.repositories.RoleRepository;
import sn.axa.apiaxacnaas.repositories.UserRepository;
import sn.axa.apiaxacnaas.util.RoleEnum;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AgenceRepository agenceRepository;
    private final PartnerRepository partnerRepository;


    public UserDTO createUser(UserCreateDTO userDTO) {
        Partner partner = partnerRepository.findById(userDTO.getPartnerId())
                .orElseThrow(()->new ResourceNotFoundException("Partner not found"));
        Role role = roleRepository.findByName(RoleEnum.USER)
                .orElseThrow(() -> new RuntimeException("Role Not Found"));
        User userEntity = userMapper.toEntity(userDTO);
        Agence agence = agenceRepository.findById(1L)
                .orElseThrow(()->new RuntimeException("Agence not found"));
        userEntity.setPassword(passwordEncoder.encode("axa@2026"));
        userEntity.setRole(role);
        userEntity.setAgence(agence);
        userEntity.setPartner(partner);
        userEntity.setIsActive(true);
        User savedUserEntity = userRepository.save(userEntity);
        return userMapper.toDTO(savedUserEntity);

    }

    public Map<String, Object> login(LoginDTO request) {
        System.out.println("EMAIL: "+ request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("❌ Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Bad credentials");

        User userEntity = authenticateAndGenerateToken(request);
        String token = jwtService.generateToken(new UserDetailsImpl(userEntity));
        UserDTO responseDTO = userMapper.toDTO(userEntity);
        return Map.of(
                "token",token,
                "user", responseDTO

        );
    }



    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email" + authentication.getName()));



    }
    public UserDTO getPublicUser(String email){
        User currentUser = null;
        if(email==null){
            currentUser = getCurrentUser();
        }
        else{
            currentUser=userRepository.findByEmail(email)
                    .orElseThrow(() ->new UsernameNotFoundException("User not found with email:" +email));
        }
        return userMapper.toDTO(currentUser);
    }

    public List<UserDTO> getAllUsers(){
        List<User> users= userRepository.findAll();
        return users.stream().map(userMapper::toDTO).toList();
    }

    public UserDTO getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return  userMapper.toDTO(user);
    }

    public UserDTO updateUserById(UserCreateDTO request, Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Partner partner = partnerRepository.findById(request.getPartnerId())
                .orElseThrow(()->new ResourceNotFoundException("Partner not found"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPartner(partner);
        User userSaved = userRepository.save(user);
        return  userMapper.toDTO(userSaved);
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);

    }

    public void deactivateUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(!user.getIsActive()){
            throw new RuntimeException("Utilisateur deja desactivé");
        }
        user.setIsActive(false);
        userRepository.save(user);


    }

    public void activateUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(user.getIsActive()){
            throw new RuntimeException("Utilisateur deja activé");
        }
        user.setIsActive(true);
        userRepository.save(user);


    }
    public User authenticateAndGenerateToken(LoginDTO request) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            return userRepository.findByEmail(request.getEmail())
                    .orElseThrow(()->new UsernameNotFoundException("Profile not found with email: "+request.getEmail()));

        }
        catch (RuntimeException e) {
            throw new RuntimeException("Invalid email or password"+e.getMessage());
        }

    }




}
