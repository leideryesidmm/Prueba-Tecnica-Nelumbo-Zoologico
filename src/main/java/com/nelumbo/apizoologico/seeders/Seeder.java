package com.nelumbo.apizoologico.seeders;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.nelumbo.apizoologico.entities.*;
import com.nelumbo.apizoologico.repositories.*;
import com.nelumbo.apizoologico.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RequiredArgsConstructor
@Component
public class Seeder {
    Faker faker = new Faker();
    private final CommentRepository commentRepository;
    private final ZoneRepository zoneRepository;
    private final SpeciesRepository speciesRepository;
    private final AnimalRepository animalRepository;
    private final UsersRepository usersRepository;
    private final CommentService commentService;
    FakeValuesService fakeValuesService = new FakeValuesService(
            new Locale("en-GB"), new RandomService());
    int cantiMaxComments=200;
    int maxAnswers=4;
    short cantJefes=3;
    short cantEmpleadosxJefe=7;
    short cantSpeciesxZone=2;
    short cantAnimalsxSpecies=2;
    LocalDateTime fechaIniComments=LocalDateTime.of(2023,1,1,0,0);
    LocalDateTime fechaFinComments=LocalDateTime.now();

    String pass="$2a$10$jiOBIS1O06xpyNlk2z4ICOIjtptXs3uNiXd6eSmuccNZo8oTiIJvy";
    Random random=new Random();
    public void seed() {
        List<Users> users=seedUsers();
        List<Zone> zones=seedZones(users);
        List<Species> species=seedSpecies(zones);
        List<Animal> animals=seedAnimals(species);
        List<Comment> comments = seedCommetns(users,animals);
        for (Comment c : comments) {
            seedAnswers(c,users);
        }
    }

    private List<Comment> seedCommetns(List<Users> users, List<Animal> animals) {
        List<Comment> comments = new ArrayList<>();

        for(Animal animal:animals) {
            int cantCom=random.nextInt(cantiMaxComments);
            for (int i = 0; i < cantCom; i++) {
                Comment comment=createComment(animal,users);
                comments.add(comment);
            }
        }



        return comments;
    }

    public Users getUserAleatorio(Animal animal, List<Users> users){
        for(int i=0;i<100;i++) {
            Users user=users.get(random.nextInt(users.size()));
            if((user.getRole().toString().equalsIgnoreCase(Role.JEFE.toString())
                    &&  animal.getSpecies().getZone().getJefe().getId().equals(user.getId()))
                    ||    (user.getRole().toString().equalsIgnoreCase(Role.EMPLEADO.toString())
                    &&  animal.getSpecies().getZone().getJefe().getId().equals(user.getJefe().getId()))
            ){
                return user;
            }
        }
        return null;
    }
    public Comment createComment(Animal animal, List<Users> users) {
        Users user=getUserAleatorio(animal,users);

        Comment comment=new Comment();
        comment.setAnimal(animal);
        comment.setUser(user);

        comment.setDate(faker.date().between(
                Date.from(fechaIniComments.atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(fechaFinComments.atZone(ZoneId.systemDefault()).toInstant())).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());

        comment.setMenssage(faker.lorem().characters());
        Comment savedComment=this.commentRepository.save(comment);
        if(user.getRole().toString().equalsIgnoreCase(Role.EMPLEADO.toString())||user.getRole().toString().equalsIgnoreCase(Role.ADMIN.toString()))
        {
            this.commentService.sendMail(savedComment);
        }
        return savedComment;
    }

    private void seedAnswers(Comment c, List<Users> users) {
        int cantAnswer=random.nextInt(maxAnswers);
        for (int i = 0; i < cantAnswer; i++) {
            Comment comment = new Comment();
            comment.setUser(getUserAleatorio(c.getAnimal(),users));
            comment.setAnimal(c.getAnimal());
            comment.setMenssage(faker.lorem().characters(4, 60));
            Date dateIni = Date.from(c.getDate().atZone(ZoneId.systemDefault())
                    .toInstant());
            Date dateFin = new Date(dateIni.getTime() + 1000 + 60 + 60 + 24 + 2);
            comment.setDate(faker.date().between(dateIni, dateFin).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
            comment.setInitialComment(c);
            commentRepository.save(comment);
        }
    }

    private Zone seedZone(Users user) {
        Zone zone=new Zone();
        zone.setName(faker.harryPotter().character());
        zone.setJefe(user);
        return this.zoneRepository.save(zone);
    }
    private List<Zone> seedZones(List<Users> users) {
        List<Zone> zones=new ArrayList<>();
        for (Users user:users) {
            if(user.getRole().equals(Role.JEFE)){
            Zone zone=seedZone(user);
            zones.add(zone);
            }
        }
        return zones;
    }
    private Species seedSpecies1(Zone zone) {
        Species species=new Species();
        species.setZone(zone);
        species.setName(faker.animal().name());
        return this.speciesRepository.save(species);
    }
    private List<Species> seedSpecies(List<Zone> zones) {
        List<Species> species=new ArrayList<>();
        for (Zone zone:zones) {
            for (int i=0;i<cantSpeciesxZone;i++){
                Species species1=seedSpecies1(zone);
                species.add(species1);
            }
        }
        return species;
    }
    private Animal seedAnimal(Species species) {
        Animal animal=new Animal();
        animal.setSpecies(species);
        animal.setCreateDate(LocalDateTime.now());
        animal.setName(faker.name().firstName());
        return this.animalRepository.save(animal);
    }
    private List<Animal> seedAnimals(List<Species> species) {
        List<Animal> animals=new ArrayList<>();
        for (Species species1:species) {
            for (int i=0;i<cantAnimalsxSpecies;i++){
                Animal animal=seedAnimal(species1);
                animals.add(animal);
            }
        }
        return animals;
    }
    private List<Users> seedUsers() {
        List<Users> users=new ArrayList<>();

        Users admin=new Users();
        admin.setUserEmail("admin@mail.com");
        admin.setName(faker.name().fullName());
        admin.setRole(Role.ADMIN);
        admin.setDisable(false);
        admin.setJefe(null);
        admin.setPass(pass);
        this.usersRepository.save(admin);
        users.add(admin);
        for(int i=0;i<cantJefes;i++){
            Users jefe=seedUser(Role.JEFE,null);
            users.add(jefe);
            for(int y=0;y<cantEmpleadosxJefe;y++){
                Users empleado=seedUser(Role.EMPLEADO,jefe);
                users.add(empleado);
            }
        }
        return users;
    }
    public Users seedUser(Role role,Users jefe){
        Users user=new Users();
        user.setUserEmail(fakeValuesService.bothify("????##@gmail.com"));
        user.setName(faker.name().fullName());
        user.setRole(role);
        user.setDisable(false);
        user.setJefe(jefe);
        user.setPass(pass);
        return this.usersRepository.save(user);
    }


}