package server;

import server.exceptions.InvalidResourceTypeException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Base64;

public class Resource {
    static String[] types = {"text", "image", "conditional"};
    String type;
    String path;

    public Resource(String dataType, String pathToData) throws InvalidResourceTypeException {

        if(Arrays.asList(types).contains(dataType))
        {
            this.type= dataType.toLowerCase();
            if(type.equals("conditional")){
                if ((pathToData.toLowerCase()=="true")){
                    this.path=pathToData.toLowerCase();
                }
                else if(pathToData.toLowerCase()=="false")
                {
                    this.path=pathToData.toLowerCase();
                }
                else
                {
                    throw new InvalidResourceTypeException("Conditional placeholders can only have true or false values");
                }
            }else {
                this.path= pathToData;
            }
        }
        else {
            throw new InvalidResourceTypeException("Invalid Resource Type Exception");
        }

    }

    public String getType(){
        return type;
    }

    public String getData() throws Exception {
        switch (type){
            case "text":
                return path;
            case "image":
                FileInputStream fis = new FileInputStream(path);
                byte[] image_data = fis.readAllBytes();
                Base64.Encoder encoder = Base64.getEncoder();
                String encoded_image = encoder.encodeToString(image_data);

                String[] splitPath = path.split("\\.");
                String extension = splitPath[splitPath.length - 1];

                StringBuilder builder = new StringBuilder();
                builder.append("data:image/"+extension+";base64,");
                builder.append(encoded_image);

                return builder.toString();

            case "conditional" :
                return path;
            case "loop":


            default:
                return "sexy boi";
        }
    }
}
