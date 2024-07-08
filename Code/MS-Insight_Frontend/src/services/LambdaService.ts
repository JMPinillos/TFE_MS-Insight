import { lambdaRespository } from '../data/persistence/InjectionDependencyRepository';

export const activateLambda = async () => {
    return await lambdaRespository.activate();
};