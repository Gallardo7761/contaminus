import PropTypes from 'prop-types';
import { Accordion } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

const ApiDocs = ({ json }) => {
  if (!json) return <p className="text-muted">No hay documentación disponible.</p>;

  const renderEndpoints = (endpoints) => (
    <Accordion>
      {endpoints.map((ep, index) => (
        <Accordion.Item eventKey={index.toString()} key={index}>
          <Accordion.Header>
            <span className={`badge bg-${getMethodColor(ep.method)} me-2 text-uppercase`}>{ep.method}</span>
            <code>{ep.path}</code>
          </Accordion.Header>
          <Accordion.Body>
            {ep.description && <p className="mb-2">{ep.description}</p>}

            {ep.params?.length > 0 && (
              <div className="d-flex flex-column gap-2 mt-3">
                {ep.params.map((param, i) => (
                  <div key={i} className="bg-light border rounded px-3 py-2">
                    <div className="d-flex justify-content-between flex-wrap mb-1">
                      <strong>{param.name}</strong>
                      <span className="badge bg-secondary">{param.in}</span>
                    </div>
                    <div className="small text-muted">
                      <div><strong>Tipo:</strong> {param.type}</div>
                      <div><strong>¿Requerido?:</strong> {param.required ? 'Sí' : 'No'}</div>
                      {param.description && <div><strong>Descripción:</strong> {param.description}</div>}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </Accordion.Body>
        </Accordion.Item>
      ))}
    </Accordion>
  );

  return (
    <div className="container p-4 bg-white rounded-4 border">
      <h1 className="fw-bold mb-5 text-dark">{json.name} <small className="text-muted fs-5">v{json.version}</small></h1>

      <h3 className="mb-3 text-dark">API de Lógica</h3>
      {renderEndpoints(json.logic_api)}

      <h3 className="mb-3 text-dark mt-5">API de Datos (Raw)</h3>
      {renderEndpoints(json.raw_api)}
    </div>
  );
};

const getMethodColor = (method) => {
  switch (method.toUpperCase()) {
    case 'GET': return 'success';
    case 'POST': return 'primary';
    case 'PUT': return 'warning';
    case 'DELETE': return 'danger';
    default: return 'secondary';
  }
};

ApiDocs.propTypes = {
  json: PropTypes.shape({
    name: PropTypes.string.isRequired,
    version: PropTypes.string.isRequired,
    logic_api: PropTypes.array,
    raw_api: PropTypes.array
  }).isRequired
};

export default ApiDocs;
