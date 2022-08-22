import React, {useEffect, useState} from 'react';
import './mainContent.css';
import PageHeader from "./PageHeader";
import AdminEditForm from "./AdminEditForm";
import DeleteConfirmModal from "./DeleteConfirmModal";
import TableMetadata from "../TableMetadata";
import {useParams} from "react-router-dom";

const calculateRange = (data, rowsPerPage) => {
    const range = [];
    const num = Math.ceil(data.length / rowsPerPage);
    for (let i = 1; i <= num; i++) {
        range.push(i);
    }
    return range;
}

const sliceData = (data, page, rowsPerPage) => {
    return data.slice((page - 1) * rowsPerPage, page * rowsPerPage);
}

function MainContent({table}) {

    const [props, setProps] = useState(null);
    const [content, setContent] = useState(null);
    const [entity, setEntity] = useState(null);
    const [addNew, setAddNew] = useState(null);
    const [columns, setColumns] = useState(null);
    const [fields, setFields] = useState(null);
    const [linkAPI, setLinkAPI] = useState('');
    const [linkSearchAPI, setLinkSearchAPI] = useState('');

    const param = useParams()

        useEffect(() => {
        TableMetadata({table, setProps})
    }, [])
    useEffect(() => {

        if (props !== null) {
            setEntity(props.entity);
            setAddNew(props.havingAddNew);
            setLinkAPI(props.linkAPI);
            setLinkSearchAPI(props.searchLink);
            fetch(props.linkAPI)
                .then((response) => response.json())
                .then(data => {
                    setContent(data)
                    setItems(data);
                })
            fetch(props.linkField)
                .then(response => response.json())
                .then(data => {
                    setColumns(data)
                    setFields(data)
                })
        }
    }, [props])

    const [isShown, setShown] = useState(false)
    const [showConfirmModal, setShowConfirmModal] = useState(false)
    const [searchTerm, setSearchTerm] = useState("")

    const [items, setItems] = useState(content);
    const [page, setPage] = useState(1);
    const [pagination, setPagination] = useState([]);
    const [selectedItem, setSelectedItem] = useState(null);

    useEffect(() => {
        if (content !== null) {
            setPagination(calculateRange(content, 5));
            setItems(sliceData(content, page, 5));
        }
    }, []);

    // Search

    useEffect(() => {
        let url = ''
        if (searchTerm.length > 0) {
            url = linkSearchAPI + +searchTerm;
            fetch(url)
                .then((response) => response.json())
                .then((data) => {
                    setItems(data);
                });
        }
    }, [searchTerm]);

    // Change Page
    const __handleChangePage = (new_page) => {
        setPage(new_page);
        setItems(sliceData(content, new_page, 5));
    }

    let columnsData = null;
    if (columns !== null) columnsData = columns.map((column, index) => (
        <th className="text-center" key={index}>{column}</th>))

    function editButtonHandler(item) {
        setSelectedItem(item);
        setShown(true);
    }

    function newButtonHandler(item) {
        setSelectedItem(null);
        setShown(true);
    }

    function hideEditForm() {
        setShown(false)
    }

    function deleteButtonHandler(item) {
        setSelectedItem(item);
        setShowConfirmModal(true);
    }

    function hideConfirmModal() {

        setShowConfirmModal(false)
    }

    function reset() {
        setSelectedItem(null);
    }

    const body = (items !== null && fields !== null ?
        <tbody>

        {items.map((item, index) => (
            <>
                <tr key={index}>
                    {fields.map((field, index) =>
                        typeof item[field] !== 'object' && Array.isArray(item[field])===false
                        ?!item[field].toString().includes('https')
                                ? <td className="text-center" key={index} style={{overflow: 'hidden',whiteSpace: 'nowrap',maxWidth:'40ch',textOverflow: 'ellipsis'}}>
                                    <span>{item[field]}</span>
                                </td>
                                : <td className="text-center" key={index}>
                                    <img className="rounded-circle" src={item[field]} width="80px" height="80px"
                                         alt={item[field]}/>
                                </td>
                            : <td className="text-center" key={index} style={{overflow: 'hidden',whiteSpace: 'nowrap',maxWidth:'40ch',textOverflow: 'ellipsis'}}>
                                <span>{JSON.stringify(item[field])}</span>
                            </td>
                    )
                    }
                    <td>
                        <button type="button" className="btn btn-primary rounded-circle mx-2"
                                onClick={() => editButtonHandler(item)}><i className={"fa fa-pen-to-square"}></i>
                        </button>
                        <button type="button" className="btn btn-danger rounded-circle mx-2"
                                onClick={() => deleteButtonHandler(item)}><i className={"fa fa-trash"}></i></button>

                    </td>
                </tr>
            </>
        ))}

        </tbody>
        : null)

    return (
        <>
            {showConfirmModal && <DeleteConfirmModal onCancel={hideConfirmModal} reset={reset}
                                                     selectedID={selectedItem ? selectedItem[fields[0]] : null}
                                                     linkToAPI={linkAPI} onshow={showConfirmModal}/>}
            <div className='mainContent_ mainContent_dashboard-content'>
                <PageHeader
                    onClick={newButtonHandler}
                    btnText={addNew === 1 ? "New " + entity : null}/>
                <div className='mainContent_dashboard-content-container'>
                    <div className='mainContent_dashboard-content-header'>

                        <h2>{entity !== null ? entity.toUpperCase() : 'MY'} LIST</h2>
                        <div className='mainContent_dashboard-content-search'>
                            <input
                                type='text'
                                value={searchTerm}
                                placeholder='Search..'
                                className='mainContent_dashboard-content-input'
                                onChange={e => setSearchTerm(e.target.value)}/>
                        </div>
                    </div>
                    {isShown &&
                        <AdminEditForm columns={columns} fields={fields} entity={entity} itemSelected={selectedItem}
                                       linkToAPI={linkAPI} selectedID={selectedItem ? selectedItem[fields[0]] : null}
                                       onCancel={hideEditForm} reset={reset}/>}

                    <table>
                        <thead>
                        <tr>{columnsData}
                            <th className={"text-center"}>ACTIONS</th>
                        </tr>
                        </thead>
                        {body}
                    </table>

                    {items !== null ?
                        <div className='mainContent_dashboard-content-footer'>
                            {pagination.map((item, index) => (
                                <span
                                    key={index}
                                    className={item === page ? 'active-pagination' : 'pagination'}
                                    onClick={() => __handleChangePage(item)}>
                                {item}
                            </span>
                            ))}
                        </div>
                        :
                        <div className='mainContent_dashboard-content-footer'>
                            <span className='empty-table'>No data</span>
                        </div>
                    }
                </div>
            </div>
        </>

    )
}

export default MainContent;