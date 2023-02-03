package kr.co.vilez.share.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class TotalListShare implements Comparable<TotalListShare>{
    ShareListDto shareListDto;
    int listCnt;

    public int compareTo(TotalListShare o){
        return o.listCnt - this.listCnt;
    }
}
