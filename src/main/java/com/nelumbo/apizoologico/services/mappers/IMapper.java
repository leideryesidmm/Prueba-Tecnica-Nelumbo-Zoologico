package com.nelumbo.apizoologico.services.mappers;

public interface IMapper <I, O>{
    public O map(I in);
    public I map2(O in);

}
