import React, { useEffect, useState } from 'react';
import { deleteArticleAPI, getArticleWithPaginateAPI } from "../services/ArticleService";
import { toast } from "react-toastify";
import PaginationCustom from "../../users/components/Pagination";
import SearchItem from "../../users/components/SearchItem";
import "./ArticlePage.css";
import FormArticle from "../components/FormArticle";
import DeleteConfirmModal from "../../users/components/DeleteConfirmModal";
import ArticleTable from '../components/ArticleTable'
import { Button, Col, Row } from 'react-bootstrap';

const ArticlePage = () => {
    const [articles, setArticles] = useState([]);

    const [currentPage, setCurrentPage] = useState(1);
    const [pageSize, setPageSize] = useState(5);

    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);

    const [loading, setLoading] = useState(false);

    const [debouncedSearch, setDebouncedSearch] = useState("");
    const [search, setSearch] = useState("");

    const [show, setShow] = useState(false);
    const [selectedArticle, setSelectedArticle] = useState(null)
    const [formMode, setFormMode] = useState("create"); // "create" | "update"

    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [articleToDelete, setArticleToDelete] = useState(null);

    const handleCreateArticle = () => {
        setSelectedArticle(null);
        setFormMode("create");
        setShow(true);
    };
    const handleUpdateArticle = (article) => {
        setSelectedArticle(article)
        setFormMode("update");
        setShow(true)
    }
    const onClose = () => {
        setShow(false)
    }
    const fetchArticles = async () => {
        try {
            setLoading(true);
            const res = await getArticleWithPaginateAPI(currentPage, pageSize, debouncedSearch);
            const data = res;
            setArticles(data.content);
            setTotalPages(data.totalPages);
            setTotalElements(data.totalElements);
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };
    const handleDeleteArticle = (article) => {
        // accepts either article object or articleId
        const normalized = typeof article === "object" ? article : { id: article };
        setArticleToDelete(normalized);
        setShowDeleteModal(true);
    };
    const confirmDeleteArticle = async () => {

        try {

            await deleteArticleAPI(articleToDelete.id);

            fetchArticles();

            toast.success("Article deleted successfully");

        } catch (error) {

            toast.error("Delete failed");
            console.error("Delete article error:", error);

        } finally {

            setShowDeleteModal(false);

        }
    };
    useEffect(() => {
        fetchArticles();
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
                    article={articleToDelete}
                    onClose={() => setShowDeleteModal(false)}
                    onConfirm={confirmDeleteArticle}
                />
                <FormArticle
                    mode={formMode}
                    selectedArticle={selectedArticle}
                    show={show}
                    onClose={onClose}
                    fetchArticles={fetchArticles}

                />
                <Row className="d-flex justify-content-between">
                    <Col xs="auto">
                        <SearchItem
                            search={search}
                            onSearch={setSearch}
                        />
                    </Col>
                    <Col xs="auto">
                        <Button className="btn btn-primary" onClick={handleCreateArticle}>
                            Create Article
                        </Button>
                    </Col>
                </Row>
                <ArticleTable
                    articles={articles}
                    loading={loading}
                    handleUpdateArticle={handleUpdateArticle}
                    handleDeleteArticle={handleDeleteArticle}
                />

                <PaginationCustom
                    currentPage={currentPage}
                    pageSize={pageSize}
                    totalPages={totalPages}
                    totalElements={totalElements}
                    onPageChange={setCurrentPage}
                />
            </div >
        </>
    )
}

export default ArticlePage;