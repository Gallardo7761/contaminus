import CardContainer from "@/components/layout/CardContainer";
import LoadingIcon from "@/components/LoadingIcon";

import { useConfig } from "@/hooks/useConfig";
import { useDataContext } from "@/hooks/useDataContext";

import { DataProvider } from "@/context/DataContext";

import { useEffect, useState } from "react";
import PropTypes from "prop-types";

const Groups = () => {
    const { config, configLoading } = useConfig();

    if (configLoading || !config) return <p className="text-center my-5"><LoadingIcon /></p>;

    const replacedEndpoint = config.appConfig.endpoints.GET_GROUPS;
    const reqConfig = {
        baseUrl: `${config.appConfig.endpoints.DATA_URL}${replacedEndpoint}`,
        devicesUrl: `${config.appConfig.endpoints.DATA_URL}${config.appConfig.endpoints.GET_GROUP_DEVICES}`,
    };

    return (
        <DataProvider config={reqConfig}>
            <GroupsContent config={reqConfig} />
        </DataProvider>
    );
}

const GroupsContent = ({ config }) => {
    const { data, dataLoading, dataError, getData } = useDataContext();
    const [devices, setDevices] = useState({});

    useEffect(() => {
        if (!data || data.length === 0) return;

        const fetchDevices = async () => {
            const results = {};

            await Promise.all(data.map(async group => {
                const endpoint = config.devicesUrl
                    .replace(':groupId', group.groupId);

                try {
                    const res = await getData(endpoint);
                    results[group.groupId] = res;
                } catch (err) {
                    console.error(`Error al obtener dispositivos de ${group.groupId}:`, err);
                }
            }));

            setDevices(results);
        };

        fetchDevices();
    }, [config.devicesUrl, data, getData]);

    if (dataLoading) return <p className="text-center my-5"><LoadingIcon /></p>;
    if (dataError) return <p className="text-center my-5">Error al cargar datos: {dataError}</p>;

    return (
        <CardContainer
            links
            text
            cards={data.map(group => {
                const groupDevices = devices[group.groupId]?.data;
                const deviceCount = groupDevices?.length;

                return {
                    title: group.groupName,
                    status: `ID: ${group.groupId}`,
                    to: `/groups/${group.groupId}`,
                    styleMode: "override",
                    content: deviceCount != null
                        ? (deviceCount === 1 ? "1 dispositivo" : `${deviceCount} dispositivos`)
                        : "Cargando dispositivos...",
                    className: "col-12 col-md-6 col-lg-4",
                };
            })}
        />
    );
}
GroupsContent.propTypes = {
    config: PropTypes.shape({
        devicesUrl: PropTypes.string.isRequired,
    }).isRequired,
};

export default Groups;
