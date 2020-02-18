/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinOrdinativoIncassoHelper;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoIncassoPerChiave;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoIncassoK;

/**
 * Classe base di model per la consultazione dell'Ordinativo di incasso della registrazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 *
 */
public abstract class ConsultaRegistrazioneMovFinOrdinativoIncassoBaseModel extends ConsultaRegistrazioneMovFinOrdinativoBaseModel<OrdinativoIncasso, SubOrdinativoIncasso, ConsultaRegistrazioneMovFinOrdinativoIncassoHelper> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2142588129877408964L;
	
	@Override
	public String getConsultazioneSubpath() {
		return "OrdinativoIncasso";
	}

	/**
	 * Crea una request per il serivizio di {@link RicercaOrdinativoIncassoPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaOrdinativoIncassoPerChiave creaRequestRicercaOrdinativoIncassoPerChiave() {
		RicercaOrdinativoIncassoPerChiave req = new RicercaOrdinativoIncassoPerChiave();
		req.setRichiedente(getRichiedente());
		RicercaOrdinativoIncassoK pRicercaOrdinativoIncassoK = new RicercaOrdinativoIncassoK();
		pRicercaOrdinativoIncassoK.setBilancio(getBilancio());
		OrdinativoIncasso ordinativoIncasso = new OrdinativoIncasso();
		ordinativoIncasso.setNumero(getNumero());
		ordinativoIncasso.setAnno(getAnno());
		pRicercaOrdinativoIncassoK.setOrdinativoIncasso(ordinativoIncasso);
		req.setpRicercaOrdinativoIncassoK(pRicercaOrdinativoIncassoK);
		req.setNumPagina(0);
		req.setNumRisultatiPerPagina(ELEMENTI_PER_PAGINA_RICERCA);
		req.setEnte(getEnte());
		return req;
	}

}
