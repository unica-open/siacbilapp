/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ordinativo.incasso;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinOrdinativoIncassoHelper;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoIncassoPerChiave;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoIncassoK;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe base di modello per la prima nota integrata sull'ordinativo di incasso.
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 13/10/2015 - gestione GEN/GSA
 */
public abstract class GestioneOrdinativoIncassoPrimaNotaIntegrataBaseModel extends BaseInserisciAggiornaPrimaNotaIntegrataBaseModel<OrdinativoIncasso, ConsultaRegistrazioneMovFinOrdinativoIncassoHelper>{

	/** Per la serializzazione */
	private static final long serialVersionUID = 1939558011757331375L;
	
	private OrdinativoIncasso ordinativoIncasso;
	
	/**
	 * @return the ordinativoIncasso
	 */
	public OrdinativoIncasso getOrdinativoIncasso() {
		return ordinativoIncasso;
	}

	/**
	 * @param ordinativoIncasso the ordinativoIncasso to set
	 */
	public void setOrdinativoIncasso(OrdinativoIncasso ordinativoIncasso) {
		this.ordinativoIncasso = ordinativoIncasso;
	}
	
	@Override
	public String getConsultazioneSubpath() {
		return "OrdinativoIncasso";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaOrdinativoIncassoPerChiave}.
	 * @param ordinativo l'ordinativo per cui creare la ricerca
	 * 
	 * @return la request creata
	 */
	public RicercaOrdinativoIncassoPerChiave creaRequestRicercaOrdinativoIncassoPerChiave(Ordinativo ordinativo) {
		RicercaOrdinativoIncassoPerChiave req = creaRequest(RicercaOrdinativoIncassoPerChiave.class);
		
		RicercaOrdinativoIncassoK pRicercaOrdinativoIncassoK = new RicercaOrdinativoIncassoK();
		pRicercaOrdinativoIncassoK.setBilancio(getBilancio());
		OrdinativoIncasso oi = new OrdinativoIncasso();
		oi.setNumero(ordinativo.getNumero());
		oi.setAnno(ordinativo.getAnno());
		pRicercaOrdinativoIncassoK.setOrdinativoIncasso(oi);
		
		req.setpRicercaOrdinativoIncassoK(pRicercaOrdinativoIncassoK);
		
		req.setNumPagina(0);
		req.setNumRisultatiPerPagina(ELEMENTI_PER_PAGINA_RICERCA);
		req.setEnte(getEnte());
		
		return req;
	}
	
	/**
	 * Crea la request per richiamare il servizio di {#RicercaContiConciliazionePerClasse}
	 * @param classeDiConciliazione la classe di conciliazione
	 * @return la request creata
	 */
	public RicercaContiConciliazionePerClasse creaRequestRicercaContiConciliazionePerConciliazione(ClasseDiConciliazione classeDiConciliazione) {
		RicercaContiConciliazionePerClasse req = creaRequest(RicercaContiConciliazionePerClasse.class);
		req.setClasseDiConciliazione(classeDiConciliazione);
		req.setCapitolo(getOrdinativoIncasso().getCapitoloEntrataGestione());
		req.setSoggetto(getOrdinativoIncasso().getSoggetto());
		req.setRichiedente(getRichiedente());
		return req;
	}


}
