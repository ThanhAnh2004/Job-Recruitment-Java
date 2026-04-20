package thanhanh.job_recruitment.service;

import thanhanh.job_recruitment.domain.Role;
import thanhanh.job_recruitment.dto.request.Role.CreateRoleRequest;
import thanhanh.job_recruitment.dto.response.Role.RoleResponse;

public interface RoleService {
    RoleResponse createRole(CreateRoleRequest request);
    RoleResponse updateRole(Role role);
    RoleResponse fetchRoleById (long id);
    boolean checkExitsById(long id);
    void deleteRole(long id);
}
