package admin.demo.Dto;

import lombok.Data;

@Data
public class ReceptionBill {
    Integer userId;
    Integer roomId;
    Long checkin;//入住时间
    Long checkout;//离店时间
    Double costTotal;//总费用


    public ReceptionBill(){

        roomId=0;
        checkin=null;
        checkout=null;
        costTotal=0.0;//总费用
    }
}
