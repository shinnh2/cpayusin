package com.jbaacount.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@NoArgsConstructor
public class sliceDto<T>
{
    private List<T> data;

    private SliceInfo sliceInfo;

    public sliceDto(List<T> data, Slice slice)
    {
        this.data = data;
        this.sliceInfo = new SliceInfo(slice.getNumber(), slice.getSize(), slice.getNumberOfElements(), slice.hasNext(), slice.hasPrevious());
    }
}
