/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.crud;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.action.contotesoreria.helper.ContoTesoreriaActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.mutuo.base.BaseMutuoAction;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.pianoammortamento.BaseInserisciAggiornaMutuoModel;
import it.csi.siac.siacbilser.model.mutuo.TipoTassoMutuo;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

public abstract class BaseInserisciAggiornaMutuoAction<BIAMM extends BaseInserisciAggiornaMutuoModel> extends BaseMutuoAction<BIAMM> {

	private static final long serialVersionUID = 1931378704845217344L;

	protected transient ContoTesoreriaActionHelper contoTesoreriaActionHelper;

	@PostConstruct
	protected void postConstruct() {
		super.postConstruct();
		contoTesoreriaActionHelper = new ContoTesoreriaActionHelper(this);
	}
	
	@Override
	public void prepareEnterPage() {
		super.prepareEnterPage();
		try {
			model.setListaClasseSoggetto(soggettoActionHelper.caricaListaClasseSoggetto());
			model.setListaTipoAtto(provvedimentoActionHelper.caricaListaTipoAtto());
			model.setListaContoTesoreria(contoTesoreriaActionHelper.caricaListaContoTesoreria());
			model.setListaPeriodoMutuo(mutuoActionHelper.caricaListaPeriodoMutuo());
		}
		catch (WebServiceInvocationFailureException e) {
			throwSystemExceptionErroreDiSistema(e);
		}
	}

	@Override
	public void prepare() throws Exception {
		mutuoActionHelper.checkFaseBilancio();		

		super.prepare();
	}

	public void validateSalva() {
		try {
			mutuoActionHelper.checkFaseBilancio();

			provvedimentoActionHelper.findAttoAmministrativo(model.getMutuo().getAttoAmministrativo());
			checkCondition(model.getMutuo().getAttoAmministrativo().getUid() > 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Provvedimento"));
			
			soggettoActionHelper.findSoggetto(model.getMutuo().getSoggetto());
			checkCondition(model.getMutuo().getSoggetto().getUid() > 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto"));
			
			checkTipoTasso();
			checkCondition(model.getMutuo().getDataAtto() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data Atto Mutuo"));
			checkCondition(StringUtils.isNotBlank(model.getMutuo().getOggetto()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Oggetto"));
			checkCondition(model.getMutuo().getContoTesoreria().getUid() > 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Sottoconto bancario"));
			checkCondition(model.getMutuo().getDurataAnni() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Durata anni"));
			checkCondition(model.getMutuo().getAnnoInizio() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno inizio"));
			checkCondition(model.getMutuo().getPeriodoRimborso().getUid() > 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Periodo"));
			checkCondition(model.getMutuo().getScadenzaPrimaRata() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Scadenza Prima Rata"));
			checkCondition(model.getMutuo().getSommaMutuataIniziale() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo"));
			checkCondition(model.getMutuo().getAnnualita() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Annualita"));
		}
		finally {
			callPrepareEnterPageOnErrors();
		}
	}

	private void checkTipoTasso() {
		checkCondition(model.getMutuo().getTipoTasso() != null,	ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Tasso"));

		if (model.getMutuo().getTipoTasso() == null) {
			return;
		}
		
		if (TipoTassoMutuo.Variabile.getCodice().equals(model.getMutuo().getTipoTasso().getCodice())) {
			checkCondition(model.getMutuo().getTassoInteresseEuribor() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Euribor"));
			checkCondition(model.getMutuo().getTassoInteresseSpread() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Spread"));
			
			return;
		}

		checkCondition(model.getMutuo().getTassoInteresse() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tasso Interesse"));
	}

	public void beforeSalva() {
		if (TipoTassoMutuo.Variabile.getCodice().equals(model.getMutuo().getTipoTasso().getCodice())) {
			model.getMutuo().setTassoInteresse(model.getMutuo().getTassoInteresseEuribor().add(model.getMutuo().getTassoInteresseSpread()));
		}
	}
	
}
