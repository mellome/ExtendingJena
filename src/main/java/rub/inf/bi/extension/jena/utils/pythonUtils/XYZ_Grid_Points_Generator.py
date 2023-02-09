import numpy as np

def generate_grid(x_range, y_range, z_range, step_size):
    x = np.arange(x_range[0], x_range[1]+step_size, step_size)
    y = np.arange(y_range[0], y_range[1]+step_size, step_size)
    z = np.arange(z_range[0], z_range[1]+step_size, step_size)

    x_mesh, y_mesh, z_mesh = np.meshgrid(x, y, z)

    x_flattened = x_mesh.flatten()
    y_flattened = y_mesh.flatten()
    z_flattened = z_mesh.flatten()

    return np.column_stack((x_flattened, y_flattened, z_flattened))

def generate_grid_adv(x_range, y_range, z_range, step_size):
    grid_size = 50
    min_point = (0,0,0)
    max_point = (100,100,100)

    # Calculate the step size
    step_size = (max_point[0]-min_point[0])/grid_size

    # Initialize the grid points list
    grid_points = []

    # Generate the grid points by looping through the grid size
    for i in range(grid_size+1):
        for j in range(grid_size+1):
            for k in range(grid_size+1):
                # Calculate the grid point position
                x = min_point[0] + i*step_size
                y = min_point[1] + j*step_size
                z = min_point[2] + k*step_size
                # Append the grid point to the list
                grid_points.append((x,y,z))
    
    return grid_points

def generate_corner_grid(x_range, y_range, z_range, step_size=1):
    # grid_size = 50
    std_min_pt = Point.ByCoordinates(0,0,0)
    std_max_pt = Point.ByCoordinates(step_size,step_size,step_size)
    std_pt_set = (std_min_pt, std_max_pt)

    # Initialize the grid points list
    grid_pt_set = []
    for i in range(0, x_range):
        mod_x_min_pt = Point.ByCoordinates(std_pt_set[0].X+i*step_size, std_pt_set[0].Y, std_pt_set[0].Z)
        mod_x_max_pt = Point.ByCoordinates(std_pt_set[1].X+i*step_size, std_pt_set[1].Y, std_pt_set[1].Z)
        x_mod_pt_set = (mod_x_min_pt, mod_x_max_pt)
        grid_pt_set.append(x_mod_pt_set)

        for j in range(1, y_range):
            mod_y_min_pt = Point.ByCoordinates(x_mod_pt_set[0].X, x_mod_pt_set[0].Y+j*step_size, x_mod_pt_set[0].Z)
            mod_y_max_pt = Point.ByCoordinates(x_mod_pt_set[1].X, x_mod_pt_set[1].Y+j*step_size, x_mod_pt_set[1].Z)
            y_mod_pt_set = (mod_y_min_pt, mod_y_max_pt)
            grid_pt_set.append(y_mod_pt_set)

            for k in range(1, z_range):
                mod_z_min_pt = Point.ByCoordinates(y_mod_pt_set[0].X, y_mod_pt_set[0].Y, y_mod_pt_set[0].Z+k*step_size)
                mod_z_max_pt = Point.ByCoordinates(y_mod_pt_set[1].X, y_mod_pt_set[1].Y, y_mod_pt_set[1].Z+k*step_size)
                z_mod_pt_set = (mod_z_min_pt, mod_z_max_pt)
                grid_pt_set.append(z_mod_pt_set)
    
    return grid_pt_set

if __name__ == "__main__":
    # grid_points = generate_grid((0, 10), (0, 10), (0, 10), 1)
    grid_points = generate_corner_grid(10, 10, 10, 1)

    print(grid_points[1][0] + " " + grid_points[1][1])

    # Save the grid points to a .xyz file
    # np.savetxt("C:\\Users\\\yhe\\Documents\\Developer\\Repo\\ExtendingJena\\src\\main\\resources\\gridPoints\\sample_grid_points.xyz", grid_points, delimiter=" ", fmt="%.4f")
