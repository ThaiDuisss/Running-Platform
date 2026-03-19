import UserTable from "../components/UserTable"
import { useEffect, useState } from "react";
import { deleteUserAPI, getUserWithPaginateAPI } from "../services/UserService";
import { toast } from "react-toastify"; import PaginationCustom from "@/features/admin/users/components/Pagination";
import SearchItem from "@/features/admin/users/components/SearchItem";
import "./UserPage.css";
import UpdateUser from "../components/UpdateUser";
import DeleteConfirmModal from "../components/DeleteConfirmModal";
const UserPage = () => {
    const [users, setUsers] = useState([]);

    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize, setPageSize] = useState(5);

    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);

    const [loading, setLoading] = useState(false);

    const [debouncedSearch, setDebouncedSearch] = useState("");
    const [search, setSearch] = useState("");

    const [show, setShow] = useState(false);
    const [selectedUser, setSelectedUSer] = useState("")

    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [userToDelete, setUserToDelete] = useState(null);

    const handleUpdateUser = (user) => {
        setSelectedUSer(user)
        setShow(true)
    }
    const onClose = () => {
        setShow(false)
    }
    const fetchUsers = async () => {

        try {

            setLoading(true);

            const res = await getUserWithPaginateAPI(currentPage, pageSize, debouncedSearch);
            console.log("Fetched users:", res);

            const data = res.data.data;

            setUsers(data.content);
            setTotalPages(data.totalPages);
            setTotalElements(data.totalElements);

        } catch (error) {

            console.error(error);

        } finally {

            setLoading(false);

        }
    };
    const handleDeleteUser = (user) => {
        setUserToDelete(user);
        setShowDeleteModal(true);
    };
    const confirmDeleteUser = async () => {

        try {

            await deleteUserAPI(userToDelete.id);

            fetchUsers();

            toast.success("User deleted successfully");

        } catch (error) {

            toast.error("Delete failed");

        } finally {

            setShowDeleteModal(false);

        }
    };
    useEffect(() => {
        fetchUsers();
    }, [currentPage, pageSize, debouncedSearch]);

    useEffect(() => {

        const timer = setTimeout(() => {
            setDebouncedSearch(search);
            setCurrentPage(0)

        }, 300);
        return () => clearTimeout(timer);

    }, [search]);

    return (
        <>
            <div className="page-container">
                <DeleteConfirmModal
                    show={showDeleteModal}
                    user={userToDelete}
                    onClose={() => setShowDeleteModal(false)}
                    onConfirm={confirmDeleteUser}
                />
                <UpdateUser
                    selectedUser={selectedUser}
                    show={show}
                    onClose={onClose}
                    fetchUsers={fetchUsers}

                />
                <SearchItem
                    search={search}
                    onSearch={setSearch}
                />

                <UserTable
                    users={users}
                    loading={loading}
                    handleUpdateUser={handleUpdateUser}
                    handleDeleteUser={handleDeleteUser}
                />

                <PaginationCustom
                    currentPage={currentPage}
                    pageSize={pageSize}
                    totalPages={totalPages}
                    totalElements={totalElements}
                    onPageChange={setCurrentPage}
                />
            </div>
        </>
    )
}
export default UserPage
