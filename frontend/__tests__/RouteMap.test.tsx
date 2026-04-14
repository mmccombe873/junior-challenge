jest.mock('react-leaflet', () => ({
  MapContainer: ({ children }: any) => (
    <div data-testid="map-container">{children}</div>
  ),
  TileLayer: () => <div />,
  Marker: ({ children }: any) => <div>{children}</div>,
  Popup: ({ children }: any) => <div>{children}</div>,
  Polyline: () => <div />,
}));

jest.mock('leaflet', () => ({
  DivIcon: function () {},
}));

import { render, screen } from '@testing-library/react';
import RouteMap from '../src/components/RouteMap';
import { OptimisedRoute } from '../src/types';

describe('RouteMap', () => {
  it('should render placeholder message when route is null', () => {
    render(<RouteMap route={null} originCity={null} />);

    expect(screen.getByText('Route Map')).toBeInTheDocument();
    expect(
        screen.getByText('Validate a route to see it displayed on the map.')
    ).toBeInTheDocument();
  });

  it('should render a map container when route is provided', () => {
    const mockRoute = {
      stops: [
        {
          stopNumber: 1,
          city: {
            id: 'city-1',
            name: 'New York',
            country: 'USA',
            latitude: 40,
            longitude: -74,
          },
          match: {
            homeTeam: { name: 'USA' },
            awayTeam: { name: 'Mexico' },
            kickoff: '2026-06-10T12:00:00Z',
          },
        },
      ],
    };

    render(<RouteMap route={mockRoute as any} originCity={null} />);

    expect(screen.getByTestId('map-container')).toBeInTheDocument();
  });

  it('should render a marker for each stop in the route', () => {
    const mockRoute = {
      stops: [
        {
          stopNumber: 1,
          city: {
            id: 'city-1',
            name: 'New York',
            country: 'USA',
            latitude: 40,
            longitude: -74,
          },
          match: {
            homeTeam: { name: 'USA' },
            awayTeam: { name: 'Mexico' },
            kickoff: '2026-06-10T12:00:00Z',
          },
        },
        {
          stopNumber: 2,
          city: {
            id: 'city-2',
            name: 'Mexico City',
            country: 'Mexico',
            latitude: 19,
            longitude: -99,
          },
          match: {
            homeTeam: { name: 'Mexico' },
            awayTeam: { name: 'Canada' },
            kickoff: '2026-06-11T12:00:00Z',
          },
        },
        {
          stopNumber: 3,
          city: {
            id: 'city-3',
            name: 'Toronto',
            country: 'Canada',
            latitude: 43,
            longitude: -79,
          },
          match: {
            homeTeam: { name: 'Canada' },
            awayTeam: { name: 'USA' },
            kickoff: '2026-06-12T12:00:00Z',
          },
        },
      ],
    };

    render(<RouteMap route={mockRoute as any} originCity={null} />);

    const markers = screen.getAllByText(/New York|Mexico City|Toronto/);
    expect(markers.length).toBe(3);
  });

  it('should handle route with empty stops array', () => {
    const mockRoute = {
        stops: [],
    };

    render(<RouteMap route={mockRoute as any} originCity={null} />);

    expect(screen.getByTestId('map-container')).toBeInTheDocument();

    expect(screen.queryAllByTestId('marker')).toHaveLength(0);
  });
});
