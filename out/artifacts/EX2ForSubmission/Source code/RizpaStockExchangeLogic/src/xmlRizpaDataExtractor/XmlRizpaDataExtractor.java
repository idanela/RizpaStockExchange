package xmlRizpaDataExtractor;

import generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class XmlRizpaDataExtractor {

    public static final String packageToReadFrom = "generated";

   public static  RizpaStockExchangeDescriptor getStocks(String path, AtomicBoolean hasSameCompany, AtomicBoolean hasSameName,AtomicBoolean hasSameUser,AtomicBoolean hasInValidStock)
   {
       RizpaStockExchangeDescriptor stocksDescriptor = null;
       try
       {
           InputStream inputStream = new FileInputStream(new File(path));
           stocksDescriptor = deserializeFrom(inputStream);
       }
       catch (JAXBException | FileNotFoundException e) {
           e.printStackTrace();
       }

       if(!checkIfContentIsValid(stocksDescriptor,hasSameCompany,hasSameName,hasSameUser,hasInValidStock))
       {
           stocksDescriptor = null;
       }

       return stocksDescriptor;
   }

   private static RizpaStockExchangeDescriptor deserializeFrom(InputStream inputStream) throws JAXBException
   {
       JAXBContext jc = JAXBContext.newInstance(packageToReadFrom);
       Unmarshaller um =jc.createUnmarshaller();
       return (RizpaStockExchangeDescriptor)um.unmarshal(inputStream);
   }

   public static boolean checkIfContentIsValid(RizpaStockExchangeDescriptor rsed, AtomicBoolean hasSameCompany, AtomicBoolean hasSameName,AtomicBoolean hasSameUser,AtomicBoolean hasInValidStock)
   {   Map<String,String> companyNames = new HashMap<>();
       Map<String,RseStock> mapStocks = new HashMap<>();
       Map<String,RseUser> mapUsers = new HashMap<>();
       List<RseStock> stocks = rsed.getRseStocks().getRseStock();
       List<RseUser> users =rsed.getRseUsers().getRseUser();
       boolean isValidFormat = true;
        boolean hasStock = false;
       for (RseStock stock:stocks)
       {
            if(mapStocks.containsKey(stock.getRseSymbol()))
           hasSameName.compareAndSet(false,true) ;
            if( companyNames.containsKey(stock.getRseCompanyName()))
           hasSameCompany.compareAndSet(false,true) ;;;
            mapStocks.put(stock.getRseSymbol(),stock);
            companyNames.put(stock.getRseCompanyName(),stock.getRseCompanyName());
       }
       for(RseUser user:users)
       {
           if(mapUsers.containsKey(user.getName()))
               hasSameUser.compareAndSet(false,true);
           mapUsers.put(user.getName(),user);

           for(RseItem Item: user.getRseHoldings().getRseItem())
           {
               if(!mapStocks.containsKey(Item.getSymbol()))
               hasInValidStock.compareAndSet(false,true);
           }
       }
          isValidFormat = !hasSameName.get() && !hasSameCompany.get() && !hasSameUser.get()&& !hasInValidStock.get();

            return isValidFormat;
        }
}
