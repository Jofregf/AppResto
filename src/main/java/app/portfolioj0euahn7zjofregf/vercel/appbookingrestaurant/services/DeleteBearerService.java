package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import org.springframework.stereotype.Service;

@Service
public class DeleteBearerService {

    public String deleteBearerText(String text){
        return text.replace("Bearer ", "");
    }
}
