package com.running_platform.mapper;

import java.util.List;

public interface BaseMapper<E, Rq, Rs> {
    Rs EntityToRespond(E e);
    Rs toRespond(Rq rq);
    E toEntity(Rq rq);
    E RsToEntity(Rs rs);

    default List<Rs> toListRes (List<E> es) {
        return es.stream().map(this::EntityToRespond).toList();
    }

    default List<E> toListE (List<Rs> rss) {
        return rss.stream().map(this::RsToEntity).toList();
    }

    default List<E> ListRequestToListE (List<Rq> rq) {
        return rq.stream().map(this::toEntity).toList();
    }
}
