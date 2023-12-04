/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.mutuo.pianoammortamento;

import it.csi.siac.siacbilser.model.mutuo.TipoVariazioneMutuo;

public class VariazionePianoMutuoModel extends BaseVariazioneMutuoModel {

	private static final long serialVersionUID = -5585767533511773420L;

	@Override
	protected TipoVariazioneMutuo getTipoVariazioneMutuo() {
		return TipoVariazioneMutuo.VariazionePiano;
	}
}
