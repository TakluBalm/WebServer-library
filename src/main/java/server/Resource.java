package server;

import server.exceptions.InvalidResourceTypeException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

public class Resource {
    static String[] types = {"text", "image", "iterable"};
    private String type;
	private Object data;

    public Resource(String dataType) throws InvalidResourceTypeException {

        if(Arrays.asList(types).contains(dataType)){
			type = dataType;
        }
        else {
            throw new InvalidResourceTypeException("Invalid Resource Type Exception");
        }

    }

	public Resource loadData(Object arg0) throws IOException, InvalidResourceTypeException{
        switch (type){
            case "text":
                data = arg0.toString();
				return this;
            case "image":
				String path = arg0.toString();
                FileInputStream fis = new FileInputStream(path);
                byte[] image_data = fis.readAllBytes();
                Base64.Encoder encoder = Base64.getEncoder();
                String encoded_image = encoder.encodeToString(image_data);

                String[] splitPath = path.split("\\.");
                String extension = splitPath[splitPath.length - 1];

                StringBuilder builder = new StringBuilder();
                builder.append("data:image/"+extension+";base64,");
                builder.append(encoded_image);

                data = builder.toString();
				return this;
            case "iterable" :
				if(!(arg0 instanceof Iterable)){
					throw new InvalidResourceTypeException("Data does not match resourceType");
				}
                data =  arg0;
				return this;
            default:
				return this;
        }
	}

    public String getType(){
        return type;
    }

    public Object getData() throws Exception {
		return data;
    }
}
