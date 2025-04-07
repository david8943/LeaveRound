interface BaseDandelionLocation {
  latitude: number;
  longitude: number;
}

export interface WhiteDandelionLocation extends BaseDandelionLocation {
  dandelionId: number;
}

export interface GoldDandelionLocation extends BaseDandelionLocation {
  goldDandelionId: number;
}
