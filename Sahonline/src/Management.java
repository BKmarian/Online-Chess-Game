public class Management {
	private static final int dycal[] = { 2, 2, -2, -2, 1, -1, 1, -1 };
	private static final int dxcal[] = { 1, -1, 1, -1, 2, 2, -2, -2 };
	private static final int dxrege[] = { 1, -1, 1, -1, 1, 0, -1, 0 };
	private static final int dyrege[] = { 1, 1, -1, -1, 0, -1, 0, 1 };

	public static int[][] nextmove(String nume, int x, int y, int r,
			int piespoz[][]) {
		int i, j;
		int mutari[][] = new int[Table.rows][Table.columns];
		int xx, yy;
		for (i = 0; i <= 7; i++)
			for (j = 0; j <= 7; j++)
				mutari[i][j] = 0;
		switch (nume.substring(1)) {
		case "pion":
			if ((x == 1 && r == 1) || (x == 6 && r == -1)) {
				xx = x + 2 * r;
				yy = y;
				if (piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
			}
			// System.out.println(piespoz[ 1 ][ 4 ]);
			xx = x + 1 * r;
			yy = y + 1 * r;
			if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
					&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
				mutari[xx][yy] = 1;
			}

			xx = x + 1 * r;
			yy = y - 1 * r;
			if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
					&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
				mutari[xx][yy] = 1;
			}

			xx = x + 1 * r;
			yy = y;
			if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
					&& piespoz[xx][yy] == 0) {
				mutari[xx][yy] = 1;
			}
			break;
		case "cal":
			for (i = 0; i <= 7; i++) {
				xx = x + dxcal[i];
				yy = y + dycal[i];
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] != piespoz[x][y])
					mutari[xx][yy] = 1;

			}
			break;
		case "nebun":
			xx = x;
			yy = y;
			for (i = 0; i <= 7; i++) {
				xx = xx + 1;
				yy = yy + 1;

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
					mutari[xx][yy] = 1;
					break;
				}

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == piespoz[x][y])
					break;
			}

			xx = x;
			yy = y;
			for (i = 0; i <= 7; i++) {
				xx = xx + 1;
				yy = yy - 1;

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
					mutari[xx][yy] = 1;
					break;
				}
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == piespoz[x][y])
					break;
			}

			xx = x;
			yy = y;
			for (i = 0; i <= 7; i++) {
				xx = xx - 1;
				yy = yy + 1;

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
					mutari[xx][yy] = 1;
					break;
				}
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == piespoz[x][y])
					break;
			}

			xx = x;
			yy = y;
			for (i = 0; i <= 7; i++) {
				xx = xx - 1;
				yy = yy - 1;

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
					mutari[xx][yy] = 1;
					break;
				}
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == piespoz[x][y])
					break;
			}
			break;
		case "tura":
			xx = x;
			yy = y;
			for (i = 0; i <= 7; i++) {
				//xx = xx;
				yy = yy + 1;

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
					mutari[xx][yy] = 1;
					break;
				}
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == piespoz[x][y])
					break;
			}

			xx = x;
			yy = y;
			for (i = 0; i <= 7; i++) {
				xx = xx + 1;
				//yy = yy;

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
					mutari[xx][yy] = 1;
					break;
				}
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == piespoz[x][y])
					break;
			}

			xx = x;
			yy = y;
			for (i = 0; i <= 7; i++) {
				xx = xx - 1;
				//yy = yy;

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
					mutari[xx][yy] = 1;
					break;
				}
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == piespoz[x][y])
					break;
			}

			xx = x;
			yy = y;
			for (i = 0; i <= 7; i++) {
				//xx = xx;
				yy = yy - 1;

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
					mutari[xx][yy] = 1;
					break;
				}
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == piespoz[x][y])
					break;
			}
			break;
		case "regina":
			xx = x;
			yy = y;
			for (i = 0; i <= 7; i++) {
				xx = xx + 1;
				yy = yy + 1;

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
					mutari[xx][yy] = 1;
					break;
				}

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == piespoz[x][y])
					break;
			}

			xx = x;
			yy = y;
			for (i = 0; i <= 7; i++) {
				xx = xx + 1;
				yy = yy - 1;

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
					mutari[xx][yy] = 1;
					break;
				}
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == piespoz[x][y])
					break;
			}

			xx = x;
			yy = y;
			for (i = 0; i <= 7; i++) {
				xx = xx - 1;
				yy = yy + 1;

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
					mutari[xx][yy] = 1;
					break;
				}
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == piespoz[x][y])
					break;
			}

			xx = x;
			yy = y;
			for (i = 0; i <= 7; i++) {
				xx = xx - 1;
				yy = yy - 1;

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
					mutari[xx][yy] = 1;
					break;
				}
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == piespoz[x][y])
					break;
			}

			xx = x;
			yy = y;
			for (i = 0; i <= 7; i++) {
				//xx = xx;
				yy = yy + 1;

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
					mutari[xx][yy] = 1;
					break;
				}
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == piespoz[x][y])
					break;
			}

			xx = x;
			yy = y;
			for (i = 0; i <= 7; i++) {
				xx = xx + 1;
				//yy = yy;

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
					mutari[xx][yy] = 1;
					break;
				}
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == piespoz[x][y])
					break;
			}

			xx = x;
			yy = y;
			for (i = 0; i <= 7; i++) {
				xx = xx - 1;
				//yy = yy;

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
					mutari[xx][yy] = 1;
					break;
				}
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == piespoz[x][y])
					break;
			}

			xx = x;
			yy = y;
			for (i = 0; i <= 7; i++) {
				//xx = xx;
				yy = yy - 1;

				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] * (-1) == piespoz[x][y]) {
					mutari[xx][yy] = 1;
					break;
				}
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == 0)
					mutari[xx][yy] = 1;
				if (xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7
						&& piespoz[xx][yy] == piespoz[x][y])
					break;
			}
			break;
		case "rege":
				for (i = 0; i <= 7; i++) {
					xx = x + dxrege[i];
					yy = y + dyrege[i];
					if (xx >= 0
							&& xx <= 7
							&& yy >= 0
							&& yy <= 7
							&& (piespoz[xx][yy] * (-1) == piespoz[x][y] || piespoz[xx][yy] == 0))
						mutari[xx][yy] = 1;
				}
		break;
		default:
			System.out.println("Eroare nume piesa " + nume);
			break;
		}
		return mutari;
	}
}
