package com.itwasneo.quarkusapp;

import com.itwasneo.quarkusapp.repository.RedisRepository;
import com.itwasneo.quarkusapp.model.Human;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("human")
public class App 
{
    @Inject
    RedisRepository<Human> redisRepository;
    
    @GET
    @Path("{key}")
    public Human get(@PathParam("key") String key) {
        return redisRepository.findByKeys(Human.class, key).orElse(null);
    }

    @POST
    public boolean set(Human human) {
       return redisRepository.save(human, human.name());
    }
}
