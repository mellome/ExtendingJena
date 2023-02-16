import numpy as np

class Object:
    def __init__(self, min_x, max_x, min_y, max_y, data):
        self.min_x = min_x
        self.max_x = max_x
        self.min_y = min_y
        self.max_y = max_y
        self.data = data

def wkt_to_bounding_box(wkt):
    # Split the WKT string into individual objects
    objects = wkt.split('),(')

    # Initialize a list to store the bounding box information for each object
    bounding_boxes = []

    for obj in objects:
        # Split the object into individual coordinates
        coords = [tuple(map(float, coord.split())) for coord in obj.strip('(),').split(',')]

        # Extract the minimum and maximum x and y coordinates
        min_x = min(coord[0] for coord in coords)
        max_x = max(coord[0] for coord in coords)
        min_y = min(coord[1] for coord in coords)
        max_y = max(coord[1] for coord in coords)

        # Store the bounding box information for the current object
        bounding_boxes.append((min_x, max_x, min_y, max_y))

    # Return the list of bounding boxes
    return bounding_boxes

def sweep_and_prune(objects, condition):
    # Sort objects by minimum x-coordinate
    objects.sort(key=lambda obj: obj.min_x)

    # Initialize active list of objects
    active = []

    # Sweep from left to right
    for obj in objects:
        # Remove objects from active list that are no longer overlapping with current object
        active = [o for o in active if o.max_x >= obj.min_x]

        # Compare overlapping objects based on condition
        for o in active:
            if obj.min_y <= o.max_y and obj.max_y >= o.min_y:
                if condition(obj.data, o.data):
                    obj.data, o.data = o.data, obj.data

        # Add current object to active list
        active.append(obj)

    # Sort objects by minimum y-coordinate
    objects.sort(key=lambda obj: obj.min_y)

    # Initialize active list of objects
    active = []

    # Sweep from bottom to top
    for obj in objects:
        # Remove objects from active list that are no longer overlapping with current object
        active = [o for o in active if o.max_y >= obj.min_y]

        # Compare overlapping objects based on condition
        for o in active:
            if obj.min_x <= o.max_x and obj.max_x >= o.min_x:
                if condition(obj.data, o.data):
                    obj.data, o.data = o.data, obj.data

        # Add current object to active list
        active.append(obj)

    # Return sorted objects
    return objects

def affine_trans(point):
    translationMatrix = np.array([[1, 0, 0, 2],
                              [0, 1, 0, 3],
                              [0, 0, 1, 4],
                              [0, 0, 0, 1]])
    return translationMatrix.dot(point) 

if __name__ == "__main__":
    point = np.array([1, 2, 3, 1])
    print(affine_trans(point))