package com.nelumbo.apizoologico.seeders;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.nelumbo.apizoologico.entities.*;
import com.nelumbo.apizoologico.repositories.*;
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
    int cantiComments=50;
    int maxAnswers=3;
    String pass="$2a$10$jiOBIS1O06xpyNlk2z4ICOIjtptXs3uNiXd6eSmuccNZo8oTiIJvy";
    Random random=new Random();
    public void seed() {
        List<Users> users=seedUser();
        Zone zone=seedZone(users.get(0));
        Species species=seedSpecies(zone);
        Animal animal=seedAnimal(species);
        List<Comment> comments = seedCommetns(users.get(random.nextInt(2)),animal);
        for (Comment c : comments) {
            seedAnswers(c);
        }
    }

    private List<Comment> seedCommetns(Users user, Animal animal) {
        List<Comment> comments = new ArrayList<>();

        for (int i = 0; i <= cantiComments; i++) {
            Comment comment = new Comment();
            comment.setUser(user);
            comment.setAnimal(animal);
            comment.setMenssage(faker.lorem().characters(4, 60));
            comment.setDate(faker.date().birthday().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
            comment = commentRepository.save(comment);
            comments.add(comment);
        }
        return comments;
    }

    private void seedAnswers(Comment c) {
        int cantAnswer=random.nextInt(maxAnswers);
        for (int i = 0; i < cantAnswer; i++) {
            Comment comment = new Comment();
            comment.setUser(c.getUser());
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
        zone.setName("Reptiles");
        zone.setJefe(user);
        return this.zoneRepository.save(zone);
    }
    private Species seedSpecies(Zone zone) {
        Species species=new Species();
        species.setZone(zone);
        species.setName(faker.animal().name());
        return this.speciesRepository.save(species);
    }
    private Animal seedAnimal(Species species) {
        Animal animal=new Animal();
        animal.setSpecies(species);
        animal.setCreateDate(LocalDateTime.now());
        animal.setName(faker.name().firstName());
        return this.animalRepository.save(animal);
    }
    private List<Users> seedUser() {
        List<Users> users=new ArrayList<>();
        Users user=new Users();
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());

        user.setUserEmail(fakeValuesService.bothify("????##@gmail.com"));
        user.setName(faker.name().fullName());
        user.setRole(Role.JEFE);
        user.setDisable(false);
        user.setPass(pass);
        user=this.usersRepository.save(user);
        users.add(user);

        Users user2=new Users();
        user2.setUserEmail(fakeValuesService.bothify("????##@gmail.com"));
        user2.setName(faker.name().fullName());
        user2.setRole(Role.EMPLEADO);
        user2.setDisable(false);
        user2.setJefe(user);
        user2.setPass(pass);
        user2=this.usersRepository.save(user2);
        users.add(user2);

        Users user3=new Users();
        user3.setUserEmail("admin@mail.com");
        user3.setName(faker.name().fullName());
        user3.setRole(Role.ADMIN);
        user3.setDisable(false);
        user3.setJefe(null);
        user3.setPass(pass);
        this.usersRepository.save(user3);


        return users;
    }



}