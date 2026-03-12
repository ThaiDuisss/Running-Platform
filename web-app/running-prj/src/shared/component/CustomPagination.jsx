import React from 'react'
import { Pagination } from 'react-bootstrap';

const CustomPagination = ({ activePage, totalPages, onPageChange }) => {

    let items = [];
    for (let number = 1; number <= totalPages; number++) {
        items.push(
            <Pagination.Item key={number} active={number === activePage} onClick={() => onPageChange(number)}>
                {number}
            </Pagination.Item>,
        );
    }
    return (
        <div>
            <Pagination>{items}</Pagination>
            <br />

            <Pagination size="lg">{items}</Pagination>
            <br />

            <Pagination size="sm">{items}</Pagination>
        </div>
    )
}

export default CustomPagination
