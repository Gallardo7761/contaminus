import CardContainer from "@/components/layout/CardContainer";
import LoadingIcon from "@/components/LoadingIcon";

import { useParams } from "react-router-dom";
import { useConfig } from "@/hooks/useConfig";
import { useDataContext } from "@/hooks/useDataContext";

import { DataProvider } from "@/context/DataContext";

const GroupView = () => {
    const { groupId } = useParams();
    const { config, configLoading } = useConfig();

    if (configLoading || !config) return <p className="text-center my-5"><LoadingIcon /></p>;

    const replacedEndpoint = config.appConfig.endpoints.GET_GROUP_DEVICES.replace(':groupId', groupId);
    const reqConfig = {
        baseUrl: `${config.appConfig.endpoints.DATA_URL}${replacedEndpoint}`,
    };

    return (
        <DataProvider config={reqConfig}>
            <GroupViewContent />
        </DataProvider>
    );   
}

const GroupViewContent = () => {
    const { data, dataLoading, dataError } = useDataContext();
    const { groupId } = useParams();

    if (dataLoading) return <p className="text-center my-5"><LoadingIcon /></p>;
    if (dataError) return <p className="text-center my-5">Error al cargar datos: {dataError}</p>;

    return (
        <CardContainer
            links
            cards={data.map(device => ({
                title: device.deviceName,
                status: `ID: ${device.deviceId}`,
                to: `/groups/${groupId}/devices/${device.deviceId}`,
                styleMode: "override",
                className: "col-12 col-md-6 col-lg-4",
            }))}
        />
    );
}

export default GroupView;