import ApiDocs from '@/components/ApiDocs';
import PropTypes from 'prop-types';
import { useEffect, useState } from 'react';

const Docs = ({ url }) => {
    const [json, setJson] = useState(null);

    useEffect(() => {
        const fetchDocs = async () => {
            try {
                const response = await fetch(url);
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                setJson(data);
            } catch (error) {
                console.error('Error fetching API docs:', error);
            }
        };

        fetchDocs();
    }, [url]);

    return (
      <ApiDocs json={json} />  
    );
}

Docs.propTypes = {
    url: PropTypes.string.isRequired,
};

export default Docs;