import Pagination from "react-bootstrap/Pagination";

const PaginationCustom = (props) => {
    const { totalElements, totalPages, currentPage, onPageChange } = props
    return (
        <>
            <Pagination>

                <Pagination.Prev
                    disabled={currentPage === 0}
                    onClick={() => onPageChange(currentPage - 1)}
                />

                {[...Array(totalPages)].map((_, index) => (

                    <Pagination.Item
                        key={index}
                        active={index === currentPage}
                        onClick={() => onPageChange(index)}
                    >

                        {index + 1}

                    </Pagination.Item>

                ))}

                <Pagination.Next
                    disabled={currentPage === totalPages - 1}
                    onClick={() => onPageChange(currentPage + 1)}
                />

            </Pagination>

            <div style={{ marginTop: "10px" }}>
                Total Users: {totalElements}
            </div>
        </>
    )
}
export default PaginationCustom