/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.mutuo.pianoammortamento;

import it.csi.siac.siacbilser.model.mutuo.TipoVariazioneMutuo;

public class VariazioneTassoMutuoModel extends BaseVariazioneMutuoModel {


	private static final long serialVersionUID = -3235158240001641091L;

	@Override
	protected TipoVariazioneMutuo getTipoVariazioneMutuo() {
		return TipoVariazioneMutuo.VariazioneTasso;
	}

}
